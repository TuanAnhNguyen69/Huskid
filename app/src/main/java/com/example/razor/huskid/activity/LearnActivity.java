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
import yanzhikai.textpath.PathAnimatorListener;


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

    @BindView(R.id.play_sound)
    ImageButton playSound;

    SlideAdapter slideAdapter;
    String topic;
    List<EnglishWord> words;
    EnglishWord currentWord;
    GetMedia getMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ButterKnife.bind(this);
        topic = getIntent().getStringExtra(TOPIC);
        words = DatabaseHelper.getInstance().getTopicWords(topic);
        slideAdapter = new SlideAdapter(this, words);
        topicName.setText(topic.toUpperCase());
        getMedia = new GetMedia();
        initInfor(0);
        pager.setAdapter(slideAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initInfor(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        playSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.getInstance().playMedia(currentWord.getAudio());
            }
        });

//        word.getAnimation().setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                playSound.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_in));
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
    }

    public void initInfor(int pos) {
        currentWord = words.get(pos);
        meaning.setText(currentWord.getMean().toUpperCase());
        word.setText(currentWord.getWord().toUpperCase());
        word.startAnimation(0, 1);

        if (pos == 0) {
            getMedia.execute(words.get(pos).getAudio(), words.get(pos + 1).getAudio());
        } else if (pos != words.size() - 1) {
            getMedia.cancel(true);
            getMedia = new GetMedia();
            getMedia.execute(words.get(pos + 1).getAudio());
        } else {
            return;
        }
    }
}
