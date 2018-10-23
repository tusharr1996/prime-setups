package com.tushar.primesetups;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialize.util.SystemUtils;
import com.tushar.primesetups.base.BaseActivity;
import com.tushar.primesetups.fragments.aboutus;
import com.tushar.primesetups.fragments.fulldetails;
import com.tushar.primesetups.fragments.setup_main;
import com.tushar.primesetups.fragments.upload;
import com.tushar.primesetups.fragments.settings;
import com.tushar.primesetups.utils.ReusableCode;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends BaseActivity {


    private static final int PROFILE_SETTING = 1;

    //save our header or result

    public static Drawer result = null;


    public static String sort = "recent";
    public static String category = "all";

    public static ArrayList<String> sort_category = new ArrayList<String>();

    public static FrameLayout fragment_container;
    public static String appdata;
    public static String check = "start";
    public boolean doubleBackToExitPressedOnce = false;

    public static SharedPreferences wishlist;
    public static SharedPreferences.Editor editor;

    String currentFragment;
    fulldetails fulldetails;
    public static FragmentManager fragmentManager;

    public static String layoutmanager = "grid";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Rishabh Added this
     //   ReusableCode.getLatestVersion(this);

        wishlist = getApplicationContext().getSharedPreferences("wishlist", MODE_PRIVATE);
        editor = wishlist.edit();

        fulldetails = new fulldetails();
        fragment_container = (FrameLayout) findViewById(R.id.frame_container);
        sort_category.add(MainActivity.sort+","+MainActivity.category);
        add(new setup_main());


        MobileAds.initialize(this, "ca-app-pub-5929853811164759~1596571134");

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showAd(getApplicationContext());
//            }
//        }, 10000);

        result = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.nav_header_main)
                .withHeaderHeight(DimenHolder.fromPixel(400))
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                sort = "recent";
                                category = "all";

                                if(sort_category.size()!=0)
                                    sort_category.add(MainActivity.sort+","+MainActivity.category);

                                setup_main.searchBar.setPlaceHolder("Prime Setups");
                                add(new setup_main());


                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withName("Top 10").withIcon(FontAwesome.Icon.faw_star).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                sort = "top10";
                                category = "all";
                                sort_category.add(MainActivity.sort+","+MainActivity.category);
                                setup_main.searchBar.setPlaceHolder("Top 10");



                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withName("Favorites").withIcon(FontAwesome.Icon.faw_heart).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                sort = "wishlist";
                                category = wishlist.getString("wishlist","temp");
                                sort_category.add(MainActivity.sort+","+MainActivity.category);

                                add(new setup_main());

                                setup_main.searchBar.setPlaceHolder("Favorites");



                                return false;
                            }
                        }),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Upload Setup").withIcon(FontAwesome.Icon.faw_upload).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                sort = "upload";
                                category = "all";
                                sort_category.add(MainActivity.sort+","+MainActivity.category);

                                add(new upload());

                                return false;
                            }
                        }),
                        new SecondaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                sort = "settings";
                                category = "all";
                                sort_category.add(MainActivity.sort+","+MainActivity.category);

                                add(new settings());
                                return false;
                            }
                        }),
                        new SecondaryDrawerItem().withName("About us").withIcon(FontAwesome.Icon.faw_user).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                sort = "about";
                                category = "all";
                                sort_category.add(MainActivity.sort+","+MainActivity.category);

                                add(new aboutus());


                                return false;
                            }
                        })


                )
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        MainActivity.this.finish();
                        //return true if we have consumed the event
                        return true;
                    }
                })
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Share").withIcon(FontAwesome.Icon.faw_share).withIdentifier(10).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Prime Setups");
                                    String sAux = "Prime Setups\n";
                                    sAux = sAux + " http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName();
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "Share with"));
                                } catch (Exception e) { //e.toString();
                                }



                                return false;
                            }
                        }),
                        new SecondaryDrawerItem().withName("Rate on Play Store").withIcon(FontAwesome.Icon.faw_star).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                                }



                                return false;
                            }
                        })
                )
                .withSavedInstance(savedInstanceState)
                // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .build();

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close



        //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one



    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);

        //add the values which need to be saved from the crossFader to the bundle

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (SystemUtils.getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.menu.embedded, menu);
            menu.findItem(R.id.menu_1).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_sort).color(Color.WHITE).actionBar());
        }
        return true;
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

    //   @Override
//    public void onBackPressed() {


    //handle the back press :D close the drawer first and if the drawer is closed close the activity
//        if (crossFader != null && crossFader.isCrossFaded()) {
//            crossFader.crossFade();
//        } else {
//            switch (check) {
//                case "home":
//                    android.support.v4.app.Fragment fragment = new setup_main();
//                    MainActivity.fragment_container.removeAllViews();
//                    android.support.v4.app.FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
//                    MainActivity.fragmentManager.popBackStack();
//                    fragmentTransaction.add(R.id.frame_container, fragment).commit();
//                    break;
//                case "category":
//                    android.support.v4.app.Fragment fragment1 = new categories();
//                    MainActivity.fragment_container.removeAllViews();
//                    android.support.v4.app.FragmentTransaction fragmentTransaction1 = MainActivity.fragmentManager.beginTransaction();
//                    MainActivity.fragmentManager.popBackStack();
//                    fragmentTransaction1.add(R.id.frame_container, fragment1).commit();
//                    break;
//                case "wishlist":
//                    android.support.v4.app.Fragment fragment2 = new setup_main();
//                    MainActivity.fragment_container.removeAllViews();
//                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = MainActivity.fragmentManager.beginTransaction();
//                    MainActivity.fragmentManager.popBackStack();
//                    fragmentTransaction2.add(R.id.frame_container, fragment2).commit();
//                    break;
//                default:
//                    finishAffinity();
//                    System.exit(0);
//                    /*if (doubleBackToExitPressedOnce) {
//                        finishAffinity();
//                        System.exit(0);
//                        return;
//                    }
//
//                    this.doubleBackToExitPressedOnce = true;
//                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//                    new Handler().postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            doubleBackToExitPressedOnce = false;
//                        }
//                    }, 2000);*/
//
//                    break;
//            }
//        }
//    }

    @Override
    protected ActionBarDrawerToggle getDrawerToggle() {
        return null;
    }

    @Override
    protected DrawerLayout getDrawer() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case R.id.menu_1:

                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}