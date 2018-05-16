package com.example.razor.huskid.entity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.example.razor.huskid.helper.SoundPlayer;

import java.io.IOException;
import java.util.HashMap;

public class GetMedia extends AsyncTask<String, Long, HashMap<String, MediaPlayer>> {
    @Override
    protected HashMap<String, MediaPlayer> doInBackground(String... strings) {
        HashMap<String, MediaPlayer> mediaPlayerHashMap = new HashMap<>();
        for (String mediaLink : strings
             ) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(mediaLink);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                mediaPlayerHashMap.put(mediaLink, mediaPlayer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mediaPlayerHashMap;
    }

    @Override
    protected void onCancelled(HashMap<String, MediaPlayer> mediaPlayer) {
        super.onCancelled(mediaPlayer);
    }

    @Override
    protected void onPostExecute(HashMap<String, MediaPlayer> mediaPlayer) {
        super.onPostExecute(mediaPlayer);
        SoundPlayer.getInstance().addMedia(mediaPlayer);
    }
}
