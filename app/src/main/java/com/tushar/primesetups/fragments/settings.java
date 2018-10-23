package com.tushar.primesetups.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tushar.primesetups.MainActivity;
import com.tushar.primesetups.R;
import com.tushar.primesetups.base.BaseFragment;

import lib.kingja.switchbutton.SwitchMultiButton;

import static com.tushar.primesetups.MainActivity.editor;

/**
 * Created by Tushar on 04-07-2017.
 */

public class settings extends BaseFragment {
    CheckBox push_notification;
    SharedPreferences settings,newspushnotification;
    boolean sub;
    boolean isChecked;
    private Toolbar toolbar;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    LinearLayout cleardown,clearwish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingsfrag,container,false);
        newspushnotification =  this.getActivity().getSharedPreferences("newspush", Context.MODE_PRIVATE);
        push_notification = (CheckBox)view.findViewById(R.id.notify);
        push_notification.setChecked(newspushnotification.getBoolean("newspush1",true));

        cleardown = (LinearLayout) view.findViewById(R.id.cleardownloads);
        clearwish = (LinearLayout) view.findViewById(R.id.clearwishlist);
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.title);
        toolbarTitle.setText("Settings");


        SwitchMultiButton mSwitchMultiButton = (SwitchMultiButton) view.findViewById(R.id.switchlayout);

        if(MainActivity.layoutmanager.equals("stack")){
            mSwitchMultiButton.setSelectedTab(0);
        }
        else
            mSwitchMultiButton.setSelectedTab(1);
        mSwitchMultiButton.setText("Stack", "Grid").setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
               if(position==0){
                   MainActivity.layoutmanager = "stack";
               }
               else
                   MainActivity.layoutmanager = "grid";
            }
        });

        push_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(push_notification.isChecked())
                {
                    SharedPreferences.Editor editor = newspushnotification.edit();
                    editor.putBoolean("newspush1",true);
                    editor.apply();
                //    Toast.makeText(getContext(),"ON",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences.Editor editor =  newspushnotification.edit();
                    editor.putBoolean("newspush1",false);
                    editor.apply();
                   // Toast.makeText(getActivity().getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cleardown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MainActivity.editor = MainActivity.downloadlist.edit();
//                editor.putString("downloadlist","temp");
//                editor.commit();
                Toast.makeText(getContext(), "Downloads List Cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        clearwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.editor = MainActivity.wishlist.edit();
                editor.putString("wishlist","temp");
                editor.commit();
                Toast.makeText(getContext(), "Wishlist Cleared.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Settings");
    }

    @Override
    protected String getTitle() {
        return null;
    }
}