package com.example.razor.huskid.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.SlideAdapter;
import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.helper.DatabaseHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    SlideAdapter slideAdapter;
    String topic;
    List<EnglishWord> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ButterKnife.bind(this);
        topic = getIntent().getStringExtra(TOPIC);
        words = DatabaseHelper.getInstance().getTopicWords(topic);
        slideAdapter = new SlideAdapter(this, words);
        topicName.setText(topic.toUpperCase());
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
    }

    public void initInfor(int pos) {
        meaning.setText(words.get(pos).getMean());
        word.setText(words.get(pos).getWord());
    }
}
