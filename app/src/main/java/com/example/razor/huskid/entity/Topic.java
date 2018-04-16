package com.example.razor.huskid.entity;


import com.example.razor.huskid.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;

@Table(database = AppDatabase.class)
public class Topic {
    @Unique
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String name;

    @Column
    private String image;

    @Column
    private String gamelevel;

    @Column
    private String audio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGamelevel() {
        return gamelevel;
    }

    public void setGamelevel(String gamelevel) {
        this.gamelevel = gamelevel;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
