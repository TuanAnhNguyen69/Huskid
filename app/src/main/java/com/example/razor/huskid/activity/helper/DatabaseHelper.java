package com.example.razor.huskid.activity.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by QuocHuy on 3/19/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data_1";
    public static final String TABLE_NAME = "data";

    public static final String WORK = "work";
    public static final String MEAN = "mean";
    public static final String IMAGE = "image";
    public static final String TOPIC = "topic";
    public static final String AUDIO = "audio";

    String pathData = "";
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        pathData = context.getFilesDir().getParent() +"/databases/" + DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void copyDatabase(){
        try {
            InputStream is = context.getAssets().open(DATABASE_NAME);
            OutputStream os = new FileOutputStream(pathData);
            byte[] buffer = new byte[1024];
            int lenght = 0;

            while ((lenght = is.read(buffer)) > 0){
                os.write(buffer, 0, lenght);
            }

            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDatabasr(){
        boolean check = checkDatabase();
        if(check){
            Log.d("Connected", "Database was created");
        }else{
            Log.d("Connected", "Database not found");
            this.getWritableDatabase();
            copyDatabase();
        }
    }

    public boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(pathData, null, SQLiteDatabase.OPEN_READONLY);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
}
