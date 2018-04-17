package com.example.razor.huskid.entity;

import com.example.razor.huskid.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;

/**
 * Created by QuocHuy on 3/19/2018.
 */

@Table(database = AppDatabase.class)

public class EnglishWord {

    @Unique
    @PrimaryKey (autoincrement = true)
    private long id;

    @Column
    private String word;

    @Column
    private String mean;

    @Column
    private String image;

    @Column
    private String topic;

    @Column
    private String audio;

    @Column
    private boolean seen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public EnglishWord setWord(String word) {
        this.word = word;
        return this;
    }

    public String getMean() {
        return mean;
    }

    public EnglishWord setMean(String mean) {
        this.mean = mean;
        return this;
    }

    public String getImage() {
        return image;
    }

    public EnglishWord setImage(String image) {
        this.image = image;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public EnglishWord setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getAudio() {
        return audio;
    }

    public EnglishWord setAudio(String audio) {
        this.audio = audio;
        return this;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
