package com.foodtiny.razor.elkid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.activity.CrossWordGameActivity;
import com.foodtiny.razor.elkid.entity.EnglishWord;
import com.foodtiny.razor.elkid.helper.SoundPlayer;

import java.util.List;

import static com.foodtiny.razor.elkid.activity.HomeActivity.BG_SOUND;

public class WordTableAdapter extends ArrayAdapter<EnglishWord> {
    public WordTableAdapter(@NonNull Context context, @NonNull List<EnglishWord> objects) {
        super(context, R.layout.word_item, objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View rootView = LayoutInflater.from(getContext()).inflate(R.layout.word_item, parent, false);

        ImageButton playsound = rootView.findViewById(R.id.play);
        TextView word = rootView.findViewById(R.id.word);
        final EnglishWord englishWord = getItem(position);
        if (englishWord != null) {
            word.setText(Integer.toString(position + 1) + ". " + englishWord.getWord());
            playsound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPlayer.getInstance().pauseBackgroundMedia();
                    int resID = getContext().getResources().getIdentifier("_" + englishWord.getWord().replaceAll(" ", "").toLowerCase(), "raw", getContext().getPackageName());
                    SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                    if (!((CrossWordGameActivity) getContext()).getStorage().get(BG_SOUND).equals("OFF")) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SoundPlayer.getInstance().playBackgroundMedia();
                            }
                        }, 3000);
                    }
                }
            });
        }
        return rootView;
    }
}
