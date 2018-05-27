package com.example.razor.huskid.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.SlideAdapter;
import com.example.razor.huskid.adapter.TopicsAdapter;
import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.entity.GetMedia;
import com.example.razor.huskid.helper.DatabaseHelper;
import com.example.razor.huskid.helper.SoundPlayer;
import com.example.razor.huskid.helper.Storage;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncTextPathView;


import static com.example.razor.huskid.activity.HomeActivity.BG_SOUND;
import static com.example.razor.huskid.activity.HomeActivity.TOPIC;

public class LearnActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.topic_name)
    TextView topicName;

    @BindView(R.id.meaning)
    TextView meaning;

    @BindView(R.id.word)
    TextView word;

    @BindView(R.id.play_sound)
    ImageButton playSound;

    @BindView(R.id.next)
    ImageButton next;

    @BindView(R.id.pre)
    ImageButton pre;

    @BindView(R.id.setting)
    ImageView setting;

    @BindView(R.id.mute)
    ImageView mute;

    @BindView(R.id.about)
    ImageView about;

    @BindView(R.id.setting_layout)
    ConstraintLayout settingLayout;

    boolean settingOpen;
    Storage storage;


    SlideAdapter slideAdapter;
    String topic;
    List<EnglishWord> words;
    EnglishWord currentWord;
    //GetMedia getMedia;
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
        //getMedia = new GetMedia();
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

        playSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.getInstance().pauseBackgroundMedia();
                int resID = getResources().getIdentifier("_" + currentWord.getWord().replaceAll(" ", "").toLowerCase(), "raw", v.getContext().getPackageName());
                SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SoundPlayer.getInstance().playBackgroundMedia();
                    }
                }, 3000);
            }
        });

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

        settingOpen = false;
        storage = new Storage(this);
        settingLayout.setBackgroundResource(R.drawable.setting_back);
        mute.setVisibility(View.INVISIBLE);
        about.setVisibility(View.INVISIBLE);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingOpen) {
                    settingLayout.setBackgroundResource(R.drawable.setting_back);
                    mute.setVisibility(View.INVISIBLE);
                    about.setVisibility(View.INVISIBLE);
                    settingOpen = false;
                    return;
                }
                settingLayout.setBackgroundResource(R.drawable.setting_back_full);
                mute.setVisibility(View.VISIBLE);
                about.setVisibility(View.VISIBLE);
                settingOpen =true;
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storage.get(BG_SOUND).equals("ON")) {
                    mute.setImageResource(R.drawable.music_off);
                    SoundPlayer.getInstance().pauseBackgroundMedia();
                    storage.set(BG_SOUND, "OFF");
                    return;
                }

                mute.setImageResource(R.drawable.music_on);
                SoundPlayer.getInstance().playBackgroundMedia();
                storage.set(BG_SOUND, "ON");
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildShowCase(pager, "Touch", "Touch here to open topic menu");
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundPlayer.getInstance().pauseBackgroundMedia();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int resID = getResources().getIdentifier("_bgm2", "raw", getPackageName());
        SoundPlayer.getInstance().prepairBackgroundMedia(this, resID);
        if (!storage.get(BG_SOUND).equals("OFF")) {
            SoundPlayer.getInstance().playBackgroundMedia();
            storage.set(BG_SOUND, "ON");
            return;
        }

        mute.setImageResource(R.drawable.music_off);
    }

    private void buildFirstShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        Object showcaseView = new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .hideOnTouchOutside()
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(1)
                .build();
    }

    private void buildShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        Object showcaseView = new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .hideOnTouchOutside()
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(1)
                .build();
    }

    public void initInfor(int pos) {
//        getMedia.cancel(true);
//        getMedia = new GetMedia();
        currentWord = words.get(pos);
        meaning.setText(currentWord.getMean().toUpperCase());
        word.setText(currentWord.getWord().toUpperCase());
//
//        if (pos == 0) {
//            getMedia.execute(words.get(pos).getAudio(), words.get(pos + 1).getAudio());
//        } else if (pos != words.size() - 1) {
//            getMedia.execute(words.get(pos + 1).getAudio());
//        } else {
//            return;
//        }
    }
}
