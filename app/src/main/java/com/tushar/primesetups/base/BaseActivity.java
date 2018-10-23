package com.tushar.primesetups.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tushar.primesetups.MainActivity;

import static com.tushar.primesetups.MainActivity.result;

public abstract class BaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private AddFragmentHandler fragmentHandler;

    private final View.OnClickListener navigationBackPressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fragmentManager.popBackStack();
        }
    };

    private final FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            onBackStackChangedEvent();
        }
    };

    private void onBackStackChangedEvent() {
        syncDrawerToggleState();
    }

    private void syncDrawerToggleState() {
        ActionBarDrawerToggle drawerToggle = getDrawerToggle();
        if (drawerToggle == null) {
            return;
        }
        if (fragmentManager.getBackStackEntryCount() > 1) {
            drawerToggle.setDrawerIndicatorEnabled(false);
            drawerToggle.setToolbarNavigationClickListener(navigationBackPressListener); //pop backstack
        } else {
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(drawerToggle.getToolbarNavigationClickListener()); //open nav menu drawer
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        fragmentHandler = new AddFragmentHandler(fragmentManager);
        fragmentManager.addOnBackStackChangedListener(backStackListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        fragmentManager.removeOnBackStackChangedListener(backStackListener);
        fragmentManager = null;
        super.onDestroy();
    }

    protected void add(BaseFragment fragment) {
        fragmentHandler.add(fragment);
    }

    protected void addwithtrans(BaseFragment fragment) {
        fragmentHandler.addwithtrans(fragment);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            if(MainActivity.sort_category.size()>1){
                int x = MainActivity.sort_category.size();
            //    Toast.makeText(this, "removed  " + MainActivity.sort_category.get(x-1) , Toast.LENGTH_SHORT).show();
                MainActivity.sort_category.remove(x-1);


                switch (MainActivity.sort_category.get(x-2)){
                    case "popular,all":
                        result.setSelectionAtPosition(1,false);

                        break;
                    case "recent,all":

                        result.setSelectionAtPosition(2,false);

                        break;
                    case "normal,all":
                        result.setSelectionAtPosition(3,false);

                        break;
                    default:
                        if(MainActivity.sort_category.get(x-2).contains("wishlist")){
                            result.setSelectionAtPosition(4,false);

                        }

                        break;


                }



        //        Toast.makeText(this, " "+ MainActivity.sort_category.get(x-2) + "+|||" + MainActivity.sort_category.size(), Toast.LENGTH_SHORT).show();
            }

        if (sendBackPressToFragmentOnTop()) {

            // fragment on top consumed the back press
            return;
        }

        //let the android system handle the back press, usually by popping the fragment
        super.onBackPressed();

        //close the activity if back is pressed on the root fragment
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finishAffinity();
                    System.exit(0);
        }
        }
    }

    private boolean sendBackPressToDrawer() {


        return false;
    }

    private boolean sendBackPressToFragmentOnTop() {

        BaseFragment fragmentOnTop = fragmentHandler.getCurrentFragment();
        if (fragmentOnTop == null) {
            return false;
        }
        if (!(fragmentOnTop instanceof BackButtonSupportFragment)) {
            return false;
        }
        return ((BackButtonSupportFragment) fragmentOnTop).onBackPressed();
    }

    protected abstract ActionBarDrawerToggle getDrawerToggle();

    protected abstract DrawerLayout getDrawer();
}