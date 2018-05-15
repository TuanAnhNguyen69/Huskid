package com.example.razor.huskid.helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class SoundPlayer {
    private static final SoundPlayer ourInstance = new SoundPlayer();

    public static SoundPlayer getInstance() {
        return ourInstance;
    }
    private MediaPlayer mediaPlayer;

    private SoundPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareMedia(String mediaLink) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(mediaLink);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
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
}
