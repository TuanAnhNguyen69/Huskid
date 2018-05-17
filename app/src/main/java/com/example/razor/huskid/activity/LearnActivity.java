package com.example.razor.huskid.activity;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.SlideAdapter;
import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.entity.GetMedia;
import com.example.razor.huskid.helper.DatabaseHelper;
import com.example.razor.huskid.helper.SoundPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncTextPathView;


import static com.example.razor.huskid.activity.HomeActivity.TOPIC;

public class LearnActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.topic_name)
    TextView topicName;

    @BindView(R.id.meaning)
    TextView meaning;

    @BindView(R.id.word)
    AsyncTextPathView word;

//    @BindView(R.id.play_sound)
//    ImageButton playSound;

    @BindView(R.id.next)
    ImageButton next;

    @BindView(R.id.pre)
    ImageButton pre;


    SlideAdapter slideAdapter;
    String topic;
    List<EnglishWord> words;
    EnglishWord currentWord;
    GetMedia getMedia;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ButterKnife.bind(this);
        topic = getIntent().getStringExtra(TOPIC);
        words = DatabaseHelper.getInstance().getTopicWords(topic);
        slideAdapter = new SlideAdapter(this, words);
        topicName.setText(topic.toUpperCase());
        currentPos = 0;
        getMedia = new GetMedia();
        initInfor(0);
        pager.setAdapter(slideAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPos = position;
                initInfor(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        playSound.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SoundPlayer.getInstance().playMedia(currentWord.getAudio());
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(currentPos + 1);
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(currentPos - 1);
            }
        });
    }

    public void initInfor(int pos) {
        getMedia.cancel(true);
        getMedia = new GetMedia();
        currentWord = words.get(pos);
        meaning.setText(currentWord.getMean().toUpperCase());
        word.setText(currentWord.getWord().toUpperCase());
        word.startAnimation(0, 1);

        if (pos == 0) {
            getMedia.execute(words.get(pos).getAudio(), words.get(pos + 1).getAudio());
        } else if (pos != words.size() - 1) {
            getMedia.execute(words.get(pos + 1).getAudio());
        } else {
            return;
        }
    }
}
