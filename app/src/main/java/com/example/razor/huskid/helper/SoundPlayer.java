package com.example.razor.huskid.helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SoundPlayer{
    private static final SoundPlayer ourInstance = new SoundPlayer();

    public static SoundPlayer getInstance() {
        return ourInstance;
    }
    private MediaPlayer mediaPlayer;
    private HashMap<String, MediaPlayer> mediaList;

    private SoundPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaList = new HashMap<>();
    }

    public void playMedia(Context context, int resID) {
        MediaPlayer localMediaPlayer = MediaPlayer.create(context, resID);
        mediaPlayer = localMediaPlayer;
        if (!localMediaPlayer.isPlaying()) {
            localMediaPlayer.start();
        }
    }

    public void stopMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void addMedia(HashMap<String, MediaPlayer> mediaLinks) {
        mediaList.putAll(mediaLinks);
    }

    public void removeMedia(String mediaLink) {
        mediaList.remove(mediaLink);
    }

    public void playMedia(String mediaLink) {
        MediaPlayer mediaPlayer = mediaList.get(mediaLink);
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            this.mediaPlayer = mediaPlayer;
        }
    }
}
