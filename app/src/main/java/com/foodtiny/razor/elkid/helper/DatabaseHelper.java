package com.foodtiny.razor.elkid.helper;

import android.content.Context;

import com.foodtiny.razor.elkid.entity.EnglishWord;
import com.foodtiny.razor.elkid.entity.EnglishWord_Table;
import com.foodtiny.razor.elkid.entity.Topic;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class DatabaseHelper {

    public static DatabaseHelper databaseHelper;

    public static synchronized DatabaseHelper getInstance() {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }
        return databaseHelper;
    }

    private DatabaseHelper(){

    }

    public List<EnglishWord> getTopicWords(String topic) {
        List<EnglishWord> englishWordArrayList;
        englishWordArrayList = SQLite.select()
                .from(EnglishWord.class)
                .where(EnglishWord_Table.topic.eq(topic))
                .queryList();
        return englishWordArrayList;
    }

    public List<Topic> getAllTopics() {
        List<Topic> topicList;
        topicList = SQLite.select()
                .from(Topic.class)
                .queryList();
        return topicList;
    }
}
