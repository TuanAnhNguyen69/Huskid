package com.example.razor.huskid.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.razor.huskid.R;

import com.example.razor.huskid.fragment.CrossWordFragment;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import static com.example.razor.huskid.activity.HomeActivity.LEVEL;
import static com.example.razor.huskid.activity.HomeActivity.TOPIC;


public class GameActivity extends AppCompatActivity implements
        CrossWordFragment.OnFragmentInteractionListener {

    CrossWordFragment crossWordFragment;
    BlurPopupWindow exitBuilder;
    BlurPopupWindow winBuilder;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        exitBuilder = new BlurPopupWindow.Builder(this)
            .setContentView(R.layout.popup_exit)
            .bindClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitBuilder.dismiss();
                }
            }, R.id.no)
            .bindClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit();
                }
            }, R.id.yes)
            .setGravity(Gravity.CENTER)
            .setScaleRatio(0.2f)
            .setBlurRadius(10)
            .setTintColor(0x30000000)
            .build();

        winBuilder = new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.popup_win)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crossWordFragment.onDetach();
                        crossWordFragment = CrossWordFragment.newInstance(getIntent().getIntExtra(LEVEL, 8), getIntent().getStringExtra(TOPIC));
                        attachFragment(crossWordFragment);
                        winBuilder.dismiss();
                    }
                }, R.id.again)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exit();
                    }
                }, R.id.exit)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();

        crossWordFragment = CrossWordFragment.newInstance(getIntent().getIntExtra(LEVEL, 8), getIntent().getStringExtra(TOPIC));
        attachFragment(crossWordFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void attachFragment(Fragment crossWordFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, crossWordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        buildExitLayout();
    }

    public void exit() {
        if (exitBuilder.isShown()) {
            exitBuilder.dismiss();
        }

        if (winBuilder.isShown()) {
            winBuilder.dismiss();
        }
        crossWordFragment.onDetach();
        finish();
    }

    public void buildExitLayout() {
        exitBuilder.show();
    }

    @Override
    public void buildWinLayout() {
        winBuilder.show();
    }
}
