package com.example.razor.huskid.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.razor.huskid.R;

import com.example.razor.huskid.fragment.CrossWordFragment;
import com.example.razor.huskid.helper.SoundPlayer;
import com.example.razor.huskid.helper.Storage;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.razor.huskid.activity.HomeActivity.BG_SOUND;
import static com.example.razor.huskid.activity.HomeActivity.LEVEL;
import static com.example.razor.huskid.activity.HomeActivity.TOPIC;


public class GameActivity extends AppCompatActivity implements
        CrossWordFragment.OnFragmentInteractionListener {

    CrossWordFragment crossWordFragment;
    BlurPopupWindow exitBuilder;
    BlurPopupWindow winBuilder;

    @BindView(R.id.setting)
    ImageView setting;

    @BindView(R.id.mute)
    ImageView mute;

    @BindView(R.id.about)
    ImageView about;

    @BindView(R.id.setting_layout)
    ConstraintLayout settingLayout;

    boolean settingOpen;
    Storage storage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

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
        mute.setVisibility(View.INVISIBLE);
        about.setVisibility(View.INVISIBLE);
        settingOpen = false;
        storage = new Storage(this);
        settingLayout.setBackgroundResource(R.drawable.setting_back);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(GameActivity.this, resID);
                if (settingOpen) {
                    settingLayout.setBackgroundResource(R.drawable.setting_back);
                    mute.setVisibility(View.INVISIBLE);
                    about.setVisibility(View.INVISIBLE);
                    settingOpen = false;
                    return;
                }
                settingLayout.setBackgroundResource(R.drawable.setting_back_full);
                mute.setVisibility(View.VISIBLE);
                about.setVisibility(View.VISIBLE);
                settingOpen =true;
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(GameActivity.this, resID);
                if (storage.get(BG_SOUND).equals("ON")) {
                    mute.setImageResource(R.drawable.music_off);
                    SoundPlayer.getInstance().pauseBackgroundMedia();
                    storage.set(BG_SOUND, "OFF");
                    return;
                }

                mute.setImageResource(R.drawable.music_on);
                SoundPlayer.getInstance().playBackgroundMedia();
                storage.set(BG_SOUND, "ON");
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(GameActivity.this, resID);
            }
        });
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

    @Override
    protected void onPause() {
        super.onPause();
        SoundPlayer.getInstance().pauseBackgroundMedia();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int resID = getResources().getIdentifier("_bgm3", "raw", getPackageName());
        SoundPlayer.getInstance().prepairBackgroundMedia(this, resID);
        if (!storage.get(BG_SOUND).equals("OFF")) {
            SoundPlayer.getInstance().playBackgroundMedia();
            storage.set(BG_SOUND, "ON");
            return;
        }

        mute.setImageResource(R.drawable.music_off);
    }
}
