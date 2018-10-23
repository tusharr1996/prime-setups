package com.tushar.primesetups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tushar.primesetups.models.tempdata;

import butterknife.BindView;
import butterknife.ButterKnife;

public class setup_details extends AppCompatActivity {

    @BindView(R.id.setup_name) TextView setupname;
    @BindView(R.id.wallname) TextView wallname;
    @BindView(R.id.icname) TextView iconname;
    @BindView(R.id.widgetname) TextView widgetname;
    @BindView(R.id.launchername) TextView launchername;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.note) TextView note;

    @BindView(R.id.downloadwall) CardView walldownload;
    @BindView(R.id.downloadic) CardView icondownload;
    @BindView(R.id.downloadwidget) CardView widgetdownload;
    @BindView(R.id.downloadlauncher) CardView launcherdownload;
    @BindView(R.id.downloadbackup) CardView backupdownload;

    boolean showFAB = true;
    NestedScrollView mainview;
    LinearLayout firstview;
    Boolean botomsheetexpanded = false;
    ImageView image;
    com.tushar.primesetups.models.tempdata tempdata;

   BottomSheetBehavior behavior;
    CustomTabsIntent customTabsIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_details);
        supportPostponeEnterTransition();
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();

        image = (ImageView)findViewById(R.id.fullimage);


        String imageTransitionName = extras.getString("sharedimage");
        image.setTransitionName(imageTransitionName);

        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        intentBuilder.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        intentBuilder.setStartAnimations(getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(getApplicationContext(), android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

       customTabsIntent = intentBuilder.build();


        tempdata = new tempdata();

        note.setPaintFlags(note.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Glide.with(this).load(tempdata.getImage_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                }).into(image);

            walldownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Uri uri = Uri.parse(tempdata.getWall_url());
                    customTabsIntent.launchUrl(setup_details.this, uri);
                }
            });
        icondownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(tempdata.getIcon_url());
                customTabsIntent.launchUrl(setup_details.this, uri);
            }
        });
        widgetdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(tempdata.getWid_url());
                customTabsIntent.launchUrl(setup_details.this, uri);
            }
        });
        launcherdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(tempdata.getLauncher_url());
                customTabsIntent.launchUrl(setup_details.this, uri);
            }
        });
        backupdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(tempdata.getBc_url());
                customTabsIntent.launchUrl(setup_details.this, uri);
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(setup_details.this);
                alertDialog.setTitle("Note");
                alertDialog.setMessage(tempdata.getNote());
                alertDialog.setIcon(R.drawable.ic_alert_decagram_black_48dp);
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
        setdata();

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        final View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);

        //       Spark mspark;
//        mspark = new Spark.Builder()
//                .setView(coordinatorLayout) // View or view group
//                .setDuration(4000)
//                .setAnimList(R.drawable.custom_anim_list)
//                .build();
//        mspark.startAnimation();


       behavior = BottomSheetBehavior.from(bottomSheet);

        mainview = (NestedScrollView) findViewById(R.id.mainview);
        firstview = (LinearLayout) findViewById(R.id.first);
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_shrink);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.gmail_fab);

        fab.setVisibility(View.VISIBLE);
        fab.startAnimation(growAnimation);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(botomsheetexpanded){
                    //  Toast.makeText(context, "expanded", Toast.LENGTH_SHORT).show();
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    botomsheetexpanded = true;
                    final OvershootInterpolator interpolator = new OvershootInterpolator();
                    ViewCompat.animate(fab).
                            rotation(0f).
                            withLayer().
                            setDuration(300).
                            setInterpolator(interpolator).
                            start();

                }
                if(!botomsheetexpanded){
                    botomsheetexpanded = false;
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    final OvershootInterpolator interpolator = new OvershootInterpolator();
                    ViewCompat.animate(fab).
                            rotation(180f).
                            withLayer().
                            setDuration(300).
                            setInterpolator(interpolator).
                            start();
                    //  Toast.makeText(context, "Collapsed", Toast.LENGTH_SHORT).show();

                }
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (showFAB){

                        }
                        //  fab.startAnimation(shrinkAnimation);
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        showFAB = true;
                        botomsheetexpanded = false;
                        final OvershootInterpolator interpolator = new OvershootInterpolator();
                        ViewCompat.animate(fab).
                                rotation(0f).
                                withLayer().
                                setDuration(300).
                                setInterpolator(interpolator).
                                start();

                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        botomsheetexpanded = true;
                        showFAB = false;
                        final OvershootInterpolator interpolator1 = new OvershootInterpolator();
                        ViewCompat.animate(fab).
                                rotation(180f).
                                withLayer().
                                setDuration(300).
                                setInterpolator(interpolator1).
                                start();
                        break;

                }

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(botomsheetexpanded) {
            //  Toast.makeText(context, "expanded", Toast.LENGTH_SHORT).show();
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            botomsheetexpanded = true;
        }
        super.onBackPressed();
    }

    public void setdata(){
        setupname.setText(tempdata.getSetup_name());
        wallname.setText(tempdata.getWall_name());
        iconname.setText(tempdata.getIcon_name());
        widgetname.setText(tempdata.getWid_name());
        launchername.setText(tempdata.getLauncher_name());
        username.setText(tempdata.getDev_name());
    }
}
