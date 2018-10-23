package com.tushar.primesetups.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tushar.primesetups.R;
import com.tushar.primesetups.base.BaseFragment;

import io.github.tonnyl.spark.Spark;

public class setup_details_frag extends BaseFragment {

        Context context;
        boolean showFAB = true;
        NestedScrollView mainview;
        LinearLayout firstview;
        Boolean botomsheetexpanded = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setup_details, container, false);
        context = view.getContext();

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        final View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);

 //       Spark mspark;
//        mspark = new Spark.Builder()
//                .setView(coordinatorLayout) // View or view group
//                .setDuration(4000)
//                .setAnimList(R.drawable.custom_anim_list)
//                .build();
//        mspark.startAnimation();


        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

        mainview = (NestedScrollView) view.findViewById(R.id.mainview);
        firstview = (LinearLayout) view.findViewById(R.id.first);
        final Animation growAnimation = AnimationUtils.loadAnimation(context, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(context, R.anim.simple_shrink);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.gmail_fab);

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
        return view;
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
}
