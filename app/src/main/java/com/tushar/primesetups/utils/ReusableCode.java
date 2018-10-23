package com.tushar.primesetups.utils;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tushar.primesetups.BuildConfig;
import com.tushar.primesetups.Constants.Constants;
import com.tushar.primesetups.MainActivity;
import com.tushar.primesetups.R;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.tushar.primesetups.MainActivity.editor;

public class ReusableCode {

    static String downloadUrl ;
    static String currentVersion ;
    static String whatsnew ;
    static String validity ;



    public static void Downloadfile(final Context context, String url, String nameParam, final Boolean downloadingParam, final String appid) {
        final Context mContext = context;
        final String name = nameParam;
        final boolean[] downloading = {downloadingParam};
        final DownloadManager mgr;
        final long download_id;

       showAd(context);
      //  Toast.makeText(mContext, "   "  + appid, Toast.LENGTH_SHORT).show();
        File direct = new File(Environment.getExternalStorageDirectory()
                + Constants.directoryPath);

        if (!direct.exists()) {
            direct.mkdirs();
        }

        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/com.tk.osmthemes/" + "temp";
        File projDir = new File(dirPath);
        if (!projDir.exists())
            projDir.mkdirs();

        String checkfilestring;

        File checkfile;

        checkfilestring = Environment.getExternalStorageDirectory() + Constants.directoryPath + name + ".apk";


        checkfile = new File(checkfilestring);


        if (!checkfile.exists()) {
            boolean showMinMax = true;

            mgr = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);
            //   Toast.makeText(context, "startedddddd d d " + downloadUri.toString(), Toast.LENGTH_SHORT).show();
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(true).setTitle(name)
                    .setVisibleInDownloadsUi(false)
                    .setDestinationInExternalPublicDir(Constants.directoryPath, name + ".apk");
            request.allowScanningByMediaScanner();


            //  mgr.enqueue(request);
            download_id = mgr.enqueue(request);
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(download_id);

            Cursor cursor = mgr.query(q);
            cursor.moveToFirst();
            final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));


            new Thread(new Runnable() {

                @Override
                public void run() {

                    downloading[0] = true;
//                    Toast.makeText(context, "strtt t t", Toast.LENGTH_SHORT).show();
                    while (downloading[0]) {

                        try {
                            DownloadManager.Query q = new DownloadManager.Query();
                            q.setFilterById(download_id);

                            Cursor cursor = mgr.query(q);

                            cursor.moveToFirst();

                            final int bytes_downloaded = cursor.getInt(cursor
                                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                            int a = bytes_total / 1024;
                            a = a / 1024;


                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                downloading[0] = false;
                                scanMedia(mContext, Environment.getExternalStorageDirectory() + Constants.directoryPath);


                                installfile(mContext, name);

                            }

                            cursor.close();

                        } catch (CursorIndexOutOfBoundsException e) {
                            downloading[0] = false;

                            ((FragmentActivity) mContext).runOnUiThread(new Runnable() {
                                public void run() {




                                }
                            });

                        }

                    }


                }
            }).start();
        } else {
            Toast.makeText(mContext, "APK is already downloaded", Toast.LENGTH_SHORT).show();

        }


    }

    private static void scanMedia(Context context, String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
    }

    public static void installfile(Context context, String name) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File to = null;

        File installfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.directoryPath + name + ".apk");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri photoURI = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    installfile);
            // Uri photoURI = FileProvider.getUriForFile(downloads.this.getContext(), getActivity().getPackageName() + ".provider", new File());
            intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
        } else {
            File file1 = new File(to.getAbsolutePath());
            intent.setDataAndType(Uri.fromFile(file1), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /*
     * Increasethe downloadcount of the application at the server
     */
    public static void increasedowncount(Context context, String appId) {

        String update_url = Constants.increaseDownloadCount + "?id=" + appId;

        StringRequest stringRequest = new StringRequest(update_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        //Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (NullPointerException e) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }

    public static void showAd(final Context context) {
        final InterstitialAd mInterstitialAd;
        mInterstitialAd = new InterstitialAd(context);
        Random r = new Random();
        int i1 = r.nextInt(5 - 1) + 1;
        switch (i1) {
            case 1:
                mInterstitialAd.setAdUnitId("ca-app-pub-5929853811164759/4769529381");
                break;
            case 2:
                mInterstitialAd.setAdUnitId("ca-app-pub-5929853811164759/2251177256");
                break;
            case 3:
                mInterstitialAd.setAdUnitId("ca-app-pub-5929853811164759/2494719836");
                break;
            case 4:
                mInterstitialAd.setAdUnitId("ca-app-pub-5929853811164759/6050821463");
                break;
            default:
                mInterstitialAd.setAdUnitId("ca-app-pub-5929853811164759/6050821463");
                break;
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("65949307BF1A4435A61625F1657DBB77")
                .build();

        // Load ads into Interstitial Ads

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }




    /*
     * Get the latest version of the application from server
     */
    public static void getLatestVersion(final Context context) {

        final StringRequest stringRequest = new StringRequest(Constants.latestVersionUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response);
                    JSONObject responseJson = new JSONObject(response);
                    String appLatestVersion = responseJson.optString("latest_version");
                    downloadUrl = responseJson.optString("download_url");
                    currentVersion = context.getString(R.string.app_version);
                    whatsnew = responseJson.getString("whats_new");
                    validity = responseJson.optString("validity");

                    if (!currentVersion.equalsIgnoreCase(appLatestVersion))
                        showDownloadDialog(context, Constants.newUpdateFound, Constants.newUpdateFoundMessage,
                                R.drawable.ic_alert_decagram_black_48dp, Constants.downloadNow, Constants.notNow, downloadUrl,whatsnew,validity);

                } catch (Exception e) {
                    Log.e("getLatestVersion", e.toString());
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorLatest", error.toString());

                    }
                });
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (NullPointerException e) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }

    public static void showDownloadDialog(final Context context, String title, String message,
                                          int iconName, final String positiveTag, final String negativeText, final String downloadUrl, final String whatsnew, final String validity) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(iconName);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(positiveTag, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (positiveTag.equalsIgnoreCase(Constants.downloadNow)) {
                    Downloadfile(context, downloadUrl, String.valueOf(R.string.app_name), false,"");
                }
                dialog.dismiss();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(validity);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!new Date().after(strDate)) {
          //  Toast.makeText(context, " " + , Toast.LENGTH_SHORT).show();

        if (negativeText!=null) {
            alertDialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }}

        if(!whatsnew.equals("")){
            alertDialog.setNeutralButton("Whats new", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    whatsnewdialog(context,"Whats new",whatsnew,R.drawable.ic_alert_decagram_black_48dp,"OK");
                }
            });
        }
        alertDialog.show();
    }
    public static void whatsnewdialog(final Context context, String title, String message,
                                      int iconName, final String positiveTag){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(iconName);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(positiveTag, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                    showDownloadDialog(context, Constants.newUpdateFound, Constants.newUpdateFoundMessage,
                            R.drawable.ic_alert_decagram_black_48dp, Constants.downloadNow, Constants.notNow, downloadUrl,whatsnew,validity);


            }
        });

        alertDialog.show();
    }

}
