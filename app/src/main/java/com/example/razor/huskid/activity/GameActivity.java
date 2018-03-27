package com.example.razor.huskid.activity;

import android.Manifest;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.example.razor.huskid.R;
import com.example.razor.huskid.entity.CrossWord;
import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.fragment.CrossWordFragment;
import com.example.razor.huskid.helper.DatabaseHelper;

import java.util.List;

import butterknife.BindView;

public class GameActivity extends AppCompatActivity implements
        CrossWordFragment.OnFragmentInteractionListener {

    CrossWordFragment crossWordFragment;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
        crossWordFragment = CrossWordFragment.newInstance(10, 10);
        attachFragment(crossWordFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void attachFragment(Fragment crossWordFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, crossWordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
