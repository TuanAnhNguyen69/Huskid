package com.example.razor.huskid.helper;

import android.content.Context;

import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.entity.EnglishWord_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by QuocHuy on 3/19/2018.
 */

public class DatabaseHelper {

    public static DatabaseHelper databaseHelper;

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }
        return databaseHelper;
    }

//    private static final String DATABASE_NAME = "data_1";
//    private static final String TABLE_NAME = "data";
//
//    private static final String WORD = "word";
//    private static final String MEAN = "mean";
//    private static final String IMAGE = "image";
//    private static final String TOPIC = "topic";
//    private static final String AUDIO = "audio";
//
//    String pathData = "";
//    Context context;
//
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, 1);
//        this.context = context;
//        pathData = context.getFilesDir().getParent() +"/databases/" + DATABASE_NAME;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//    }
//
//    public void copyDatabase(){
//        try {
//            InputStream is = context.getAssets().open(DATABASE_NAME);
//            OutputStream os = new FileOutputStream(pathData);
//            byte[] buffer = new byte[1024];
//            int lenght = 0;
//
//            while ((lenght = is.read(buffer)) > 0){
//                os.write(buffer, 0, lenght);
//            }
//
//            os.flush();
//            os.close();
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void createDatabase(){
//        boolean check = checkDatabase();
//        if(check){
//            Log.d("Connected", "Database was created");
//        }else{
//            Log.d("Connected", "Database not found");
//            this.getWritableDatabase();
//            copyDatabase();
//        }
//    }
//
//    public boolean checkDatabase() {
//        SQLiteDatabase checkDB = null;
//        try {
//            checkDB = SQLiteDatabase.openDatabase(pathData, null, SQLiteDatabase.OPEN_READONLY);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        if(checkDB != null){
//            checkDB.close();
//        }
//        return checkDB != null;
//    }

    public List<EnglishWord> getTopicWords(String topic) {
        List<EnglishWord> englishWordArrayList;
        englishWordArrayList = SQLite.select()
                .from(EnglishWord.class)
                .where(EnglishWord_Table.topic.eq(topic))
                .queryList();
        return englishWordArrayList;
    }
}
