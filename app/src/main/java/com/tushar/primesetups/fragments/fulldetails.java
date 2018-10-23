package com.tushar.primesetups.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tushar.primesetups.BuildConfig;
import com.tushar.primesetups.MainActivity;
import com.tushar.primesetups.R;
import com.tushar.primesetups.base.BackButtonSupportFragment;
import com.tushar.primesetups.base.BaseFragment;

import com.tushar.primesetups.models.Result;
import com.tushar.primesetups.utils.ReusableCode;

import java.io.File;


public class fulldetails extends BaseFragment implements BackButtonSupportFragment {

    private Toolbar toolbar;
    LinearLayout imglinear;
TextView appname,appversion,appsize,readmore,category,community,developer,downtxt;
ImageView applogo;

CardView downloadbtn;
    static DownloadManager mgr;
    static long download_id;
    File from,to;

    String sslink;
    String[] ss;

    private int STORAGE_PERMISSION_CODE = 23;

    static boolean downloading;
    Result result;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.appdetails, container, false);


        toolbar = (Toolbar) view.findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.title);

        appname = (TextView)view.findViewById(R.id.appname);
        appversion = (TextView)view.findViewById(R.id.appversion);
        appsize = (TextView)view.findViewById(R.id.appsize);
        readmore = (TextView)view.findViewById(R.id.readmore);
        community = (TextView)view.findViewById(R.id.community);
        category = (TextView)view.findViewById(R.id.category);
        developer = (TextView)view.findViewById(R.id.developer);
        downtxt = (TextView)view.findViewById(R.id.downtxt);

        applogo = (ImageView) view.findViewById(R.id.applogo);
        downloadbtn = (CardView) view.findViewById(R.id.downloadbtn);
        imglinear = (LinearLayout) view.findViewById(R.id.imglinear);
//        appsResuls = new ArrayList<Result>();

   //     result = appsResuls.get(MainActivity.position);


        String dollarsign = "#";
        final String[] parts = MainActivity.appdata.split(dollarsign);




        toolbarTitle.setText(parts[1]);
        appname.setText(parts[1]);
        appversion.setText("V" + parts[2]);
        appsize.setText(parts[3]);
        category.setText("•"+ parts[4]);

        community.setText(parts[6]);
        developer.setText("Developed By:- " + parts[7]);
        sslink = parts[11];
        ss = sslink.split("@");
    //    Toast.makeText(getContext(), "  " + ss[1], Toast.LENGTH_SHORT).show();

        for(int i=0;i<ss.length;i++)
        {
            final ImageView image = new ImageView(getContext());
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,750));
            image.setPadding(7,0,0,0);
            image.setId(i);
            Glide.with(getActivity())
                    .load(ss[i])
                    .asBitmap().format(DecodeFormat.PREFER_RGB_565)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(image);


            // Adds the view to the layout
            imglinear.addView(image);
        }



//        Glide.with(getActivity())
//                .load(parts[0])
//                .asBitmap().format(DecodeFormat.PREFER_RGB_565)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .override(130,130)
//                .listener(new RequestListener<String, Bitmap>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        return false;
//                    }
//                })
//                .into(applogo);

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Osm Apps/apk");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/com.tk.osmapps/" + "temp";
        File projDir = new File(dirPath);
        if (!projDir.exists())
            projDir.mkdirs();

        String checkfilestring;

        final File checkfile;

        checkfilestring = Environment.getExternalStorageDirectory() + "/Osm Apps/apk/" + parts[1] + ".apk";

        checkfile = new File( checkfilestring );

        if (checkfile.exists()) {
            downtxt.setText("Install");
        }
        else
            downtxt.setText("Download");

        downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isReadStorageAllowed()) {
if(downtxt.getText().toString().equals("Download"))
                    ReusableCode.Downloadfile(getActivity(),parts[8],parts[1],downloading,parts[10]);
                    if(downtxt.getText().toString().equals("Install"))
                        ReusableCode.installfile(getActivity(),parts[1]);

                } else {

                    requestStoragePermission();
                }


         //  Downloadfile(parts[8],parts[1]);
            }
        });

        return view;
    }

    void Downloadfile(String url, final String name){
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Osm Apps/apk");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/com.tk.osmthemes/" + "temp";
        File projDir = new File(dirPath);
        if (!projDir.exists())
            projDir.mkdirs();

        String checkfilestring;

        File checkfile;

        checkfilestring = Environment.getExternalStorageDirectory() + "/Osm Apps/apk/" + name + ".apk";



        checkfile = new File( checkfilestring );




        if (!checkfile.exists()) {
            boolean showMinMax = true;

            mgr = (DownloadManager) fulldetails.this.getContext().getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);
            //   Toast.makeText(context, "startedddddd d d " + downloadUri.toString(), Toast.LENGTH_SHORT).show();
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(true).setTitle(name)
                    .setVisibleInDownloadsUi(false)
                    .setDestinationInExternalPublicDir("/Osm Apps/apk/",  name + ".apk");
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

                    downloading = true;
//                    Toast.makeText(context, "strtt t t", Toast.LENGTH_SHORT).show();
                    while (downloading) {

                        try {
                            DownloadManager.Query q = new DownloadManager.Query();
                            q.setFilterById(download_id);

                            Cursor cursor = mgr.query(q);

                            cursor.moveToFirst();

                            final int bytes_downloaded = cursor.getInt(cursor
                                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                            int a = bytes_total/1024;
                            a = a/1024;


                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                downloading = false;


                                //    scanMedia(Environment.getExternalStorageDirectory() + "/Osm Apps/apk/" + name + ".apk");

                                scanMedia(Environment.getExternalStorageDirectory() + "/Osm Apps/apk/");
                                installfile(name);

                            }

                            cursor.close();

                        }
                        catch (CursorIndexOutOfBoundsException e){
                            downloading = false;
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    //  Toast toast = Toast.makeText(getContext(), "errrooorrrr ", Toast.LENGTH_SHORT);
                                    //   toast.show();


                                }
                            });

                        }

                    }





                }
            }).start();
        }
        else{
            Toast.makeText(fulldetails.this.getContext(), "APK is already downloaded", Toast.LENGTH_SHORT).show();

        }



    }

    void installfile(String name){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);

        File installfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Osm Apps/apk/" + name + ".apk");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri photoURI = FileProvider.getUriForFile(fulldetails.this.getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    installfile);
            // Uri photoURI = FileProvider.getUriForFile(downloads.this.getContext(), getActivity().getPackageName() + ".provider", new File());
            intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
        } else {
            File file1 = new File(to.getAbsolutePath());
            intent.setDataAndType(Uri.fromFile(file1), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }


    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        getContext().sendBroadcast(scanFileIntent);
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "To download Apps we need Storage permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                //Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
