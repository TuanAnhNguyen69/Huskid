package com.foodtiny.razor.elkid.helper;

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
    private MediaPlayer backgroundMediaPlayer;
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

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    public void stopMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void prepairBackgroundMedia(Context context, int resID) {
        MediaPlayer localMediaPlayer = MediaPlayer.create(context, resID);
        backgroundMediaPlayer = localMediaPlayer;
        backgroundMediaPlayer.setLooping(true);
    }

    public void stopBackgroundMedia() {
        if (backgroundMediaPlayer.isPlaying()) {
            backgroundMediaPlayer.stop();
        }
    }

    public void pauseBackgroundMedia() {
        if (backgroundMediaPlayer.isPlaying()) {
            backgroundMediaPlayer.pause();
        }
    }

    public void playBackgroundMedia() {
        if (!backgroundMediaPlayer.isPlaying()) {
            backgroundMediaPlayer.start();
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
