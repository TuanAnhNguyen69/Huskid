package com.example.razor.huskid.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private Activity activity;

    public Storage(Activity activity) {
        this.activity = activity;
    }

    public Storage set(String key, String value) {
        SharedPreferences settings = this.activity.getSharedPreferences("Huskid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
        return this;
    }

    public String get(String key) {
        SharedPreferences settings = this.activity.getSharedPreferences("Huskid", Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }
}