package com.example.razor.huskid.helper;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class SoundPlayer {
    private static final SoundPlayer ourInstance = new SoundPlayer();
    private MediaPlayer mediaPlayer;

    public static SoundPlayer getInstance() {
        return ourInstance;
    }

    private SoundPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareMedia(String mediaLink) {
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

    public void stopMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}
