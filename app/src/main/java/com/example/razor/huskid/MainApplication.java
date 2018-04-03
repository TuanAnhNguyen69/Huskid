package com.example.razor.huskid;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by razor on 27/03/2018.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        FlowManager.init(this);
        super.onCreate();
    }
}
