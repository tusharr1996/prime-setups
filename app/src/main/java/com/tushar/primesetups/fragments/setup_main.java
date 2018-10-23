package com.tushar.primesetups.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.littlemango.stacklayoutmanager.StackLayoutManager;
import com.mancj.materialsearchbar.MaterialSearchBar;

import com.tushar.primesetups.Database.DatabaseHelper;
import com.tushar.primesetups.MainActivity;
import com.tushar.primesetups.PaginationAdapter;
import com.tushar.primesetups.R;
import com.tushar.primesetups.base.BackButtonSupportFragment;
import com.tushar.primesetups.base.BaseFragment;
import com.tushar.primesetups.models.Result;
import com.tushar.primesetups.utils.PaginationAdapterCallback;
//import com.tushar.primesetups.utils.PaginationScrollListenergrid;
//import com.tushar.primesetups.utils.PaginationScrollListenerstack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeoutException;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.tushar.primesetups.MainActivity.sort_category;

public class setup_main extends BaseFragment implements PaginationAdapterCallback, PaginationAdapter.OnItemClicked, PaginationAdapter.OnImageClicked, MaterialSearchBar.OnSearchActionListener, BackButtonSupportFragment {

    private static final String TAG = "MainActivity";

    public DatabaseHelper database;
    List<Result> result;

    PaginationAdapter adapter;
    GridLayoutManager gridLayoutManager;

StackLayoutManager stackLayoutManager;



    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    NotificationManager notificationManager;
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

    NotificationCompat.Builder notificationBuilder;


    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.

    public static int TOTALCOUNT = 4;


    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int STORAGE_PERMISSION_CODE = 23;

    public static String setupurl = "fetchsetups.php";
    private Toolbar toolbar;
    public static MaterialSearchBar searchBar;
    String oldsearch;
    boolean issarch;
    String searchitem, searchurl = "searchapp.php";

   static InterstitialAd mInterstitialAd;
//    static AdView mAdView;

    Context context;


    public NotificationCompat.Builder b;
    public int m;
    public NotificationManager nm;
    String filevalue;
    String appnamest;
    String dwnlink;
    String idtmp;
    String actiontype;
    String appsize;
    int downcounttmp = 0;
    private boolean downloading = false;



    public int dsize, tsize = 0;
    public String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
    SharedPreferences check;
    boolean firstrun;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.setups_main, container, false);

        initialize(view, container);

        //    checkIfFirstRun();

        int x = sort_category.size();
        String[] abc = sort_category.get(x - 1).split(",");
        MainActivity.sort = abc[0];
        if (abc.length > 1) {
            MainActivity.category = abc[1];
        } else {
            Toast.makeText(context, "Nothing here.", Toast.LENGTH_SHORT).show();
            MainActivity.category = "temp";
        }

        if (isReadStorageAllowed()) {


        } else {

            requestStoragePermission();
        }

        //    showAd(getContext());




        List<Result> results = result;
        Toast.makeText(context, "loadedd", Toast.LENGTH_SHORT).show();
        adapter.addAll(results);
      //  loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }

    private void initialize(View view, ViewGroup container) {
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        searchBar = (MaterialSearchBar) view.findViewById(R.id.searchBar);
        searchBar.setSpeechMode(true);



        context = container.getContext();

        database = new DatabaseHelper(context);
        result = database.getdata();

        notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        searchBar.setOnSearchActionListener(setup_main.this);
        //restore last queries from disk

//getcount(context);
      //  mAdView = (AdView) view.findViewById(R.id.adView);
        rv = (RecyclerView) view.findViewById(R.id.main_recycler);

        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);
        adapter = new PaginationAdapter(getActivity(), setup_main.this);
        adapter.setOnClick(setup_main.this);
        adapter.setOnImageClick(setup_main.this);



        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

stackLayoutManager = new StackLayoutManager();

if(MainActivity.layoutmanager.equals("grid"))
        rv.setLayoutManager(gridLayoutManager);
else
    rv.setLayoutManager(stackLayoutManager);

    //    new CardSnapHelper().attachToRecyclerView(rv);
        rv.setItemAnimator(new DefaultItemAnimator());
        mInterstitialAd = new InterstitialAd(getContext());
        issarch = false;

        rv.setAdapter(adapter);

        showbaner(getContext());
    }






    public static void showAd(final Context context) {

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
                .addTestDevice("BE57D129BCBFA39D7B6EC4ADDBE2BB8D")
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

    public static void showbaner(Context context){
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId("ca-app-pub-5558829543232623/1261335691");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("65949307BF1A4435A61625F1657DBB77")
                .build();

//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }});
//        mAdView.loadAd(adRequest);
    }





    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        if (!enabled) {

            oldsearch = "";
            issarch = false;
            adapter.clear();
            progressBar.setVisibility(View.VISIBLE);

        }
        //   Toast.makeText(MainActivity.this, "Search " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

        issarch = true;
        String temp;
        temp = text.toString();
        searchitem = text.toString();
        if (!temp.equals(oldsearch)) {
            if (!temp.matches("")) {

                oldsearch = temp;

                TOTALCOUNT = 4;

                adapter.clear();
                if (isNetworkConnected()) {
                    progressBar.setVisibility(View.VISIBLE);

                }

            } else {
                // Toast.makeText(this, "Please Enter Value.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {

            case MaterialSearchBar.BUTTON_SPEECH:
                //  openVoiceRecognizer();
                promptSpeechInput();

                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                if (issarch) {


                    issarch = false;

                    oldsearch = "";
                    adapter.clear();

                    progressBar.setVisibility(View.VISIBLE);

                }
                break;
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say something..");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(), "Sorry! Your device doesn\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //Toast.makeText(this, " "  + result.get(0), Toast.LENGTH_SHORT).show();
                    searchBar.enableSearch();
                    searchBar.setText(result.get(0));
                }
                break;
            }

        }
    }







    @Override
    public void onItemClick(String name) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
      //  add(new setup_details_frag());
    }


    @Override
    public void onImageClick(String imagepos, ImageView sharedimage) {
      //  addwithtrans(new setup_details_frag());

        Intent myIntent = new Intent(getContext(), com.tushar.primesetups.setup_details.class);
        myIntent.putExtra("sharedimage", ViewCompat.getTransitionName(sharedimage));


        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,sharedimage,ViewCompat.getTransitionName(sharedimage));
        startActivity(myIntent, optionsCompat.toBundle());

//        MainActivity.appdata = imagepos;
//        sort_category.add(MainActivity.sort+","+MainActivity.category);
//        int x = MainActivity.sort_category.size();
//    //    Toast.makeText(getContext(), " " + MainActivity.sort_category.get(x-1), Toast.LENGTH_SHORT).show();
//
//        add(new fulldetails());


    }








    @Override
    public void retryPageLoad() {

    }


    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
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




//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    //    super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        try {
            switch (MainActivity.sort) {
                case "all":
                    searchBar.setPlaceHolder("OsmApps");
                    break;
                case "popular":
                    searchBar.setPlaceHolder("Popular");
                    break;
                case "recent":
                    searchBar.setPlaceHolder("Recent");
                    break;
                case "GB Material Mod":
                    searchBar.setPlaceHolder("GB Material Mod");
                    break;
                case "Foud Mod":
                    searchBar.setPlaceHolder("Foud Mod");
                    break;
                case "Mods B58":
                    searchBar.setPlaceHolder("Mods B58");
                    break;
                case "GBInstagram":
                    searchBar.setPlaceHolder("GBInstagram");
                    break;

            }

        } catch (NullPointerException e) {
            getActivity().setTitle("OsmApps");
        }
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected String getTitle() {
        return null;
    }
}
