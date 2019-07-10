package com.rumpur.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rumpur.R;
import com.rumpur.Utils.CommonUtilities;
import com.rumpur.Utils.GeneralValues;
import com.rumpur.retrofit.MainAsynListener;
import com.rumpur.retrofit.MainAsyncTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainAsynListener<String> {
    static SlidingMenu menu;
    private SlideMenu slidemenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIDs();
    }

    private void getIDs() {
        CommonUtilities.actionbarImplement(MainActivity.this, "Home", " ", R.drawable.menu, 0);
        //menu barr
        menu = CommonUtilities.setSlidingMenu(MainActivity.this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidemenu = new SlideMenu(menu, MainActivity.this);
        slidemenu.setImageback(GeneralValues.get_user_image(this));
        slidemenu.setTextname(GeneralValues.get_user_name(this));
        CommonUtilities.relLayout1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relLayout1:
                menu.showMenu();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPostSuccess(String result, int flag, boolean isSucess) {

    }

    @Override
    public void onPostError(int flag) {

    }
    public void transactionFragments( Fragment fragment, int viewResource) {
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewResource, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
