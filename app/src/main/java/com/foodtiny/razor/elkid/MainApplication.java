package com.foodtiny.razor.elkid;

import android.app.Application;

import com.foodtiny.razor.elkid.database.AppDatabase;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by razor on 27/03/2018.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        getDatabasePath("Huskid.db").delete();
        //FlowManager.getDatabase(AppDatabase.NAME).reset(this);
        FlowManager.init(this);
        super.onCreate();
    }
}
