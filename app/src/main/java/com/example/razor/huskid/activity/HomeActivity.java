package com.example.razor.huskid.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.TopicsAdapter;
import com.example.razor.huskid.entity.Topic;
import com.example.razor.huskid.helper.DatabaseHelper;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    public static final String TOPIC = "topic";
    public static final String LEVEL = "level";

    @BindView(R.id.topicList)
    ListView topicListView;

    @BindView(R.id.setting)
    ImageView setting;

    @BindView(R.id.mute)
    ImageView mute;

    @BindView(R.id.about)
    ImageView about;

    @BindView(R.id.setting_layout)
    ConstraintLayout settingLayout;

    private TopicsAdapter topicsAdapter;
    boolean settingOpen;
    boolean gameLevelOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initTopicList();
        settingOpen = false;
        gameLevelOpen = false;
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
    }

    private void initTopicList() {
        topicsAdapter = new TopicsAdapter(this, DatabaseHelper.getInstance().getAllTopics());
        topicListView.setAdapter(topicsAdapter);
        topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
//                intent.putExtra(TOPIC, DatabaseHelper.getInstance().getAllTopics().get(position).getName());
//                startActivity(intent);
                onButtonClick(DatabaseHelper.getInstance().getAllTopics().get(position).getName());
            }
        });
    }

    public void onButtonClick(final String topic) {
        BlurPopupWindow builder = new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.popup_window)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoLearn(topic);
                    }
                }, R.id.btnLearn)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View gameMode = findViewById(R.id.game_mode);
                        View btnLearn = findViewById(R.id.btnLearn);
                        if (gameLevelOpen) {
                            gameMode.setVisibility(View.GONE);
                            btnLearn.setVisibility(View.VISIBLE);
                            gameLevelOpen = false;
                            return;
                        }
                        gameMode.setVisibility(View.VISIBLE);
                        btnLearn.setVisibility(View.GONE);
                        gameLevelOpen = true;
                    }
                }, R.id.btnPlay)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoGame(topic, 8);
                    }
                }, R.id.normal)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoGame(topic, 10);
                    }
                }, R.id.hard)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();

        builder.show();
    }

    public void gotoLearn(String topic) {
        Intent intent = new Intent(HomeActivity.this, LearnActivity.class);
                intent.putExtra(TOPIC, topic);
                startActivity(intent);
    }

    public void gotoGame(String topic, int level) {
        Intent intent = new Intent(HomeActivity.this, GameActivity.class);
        intent.putExtra(TOPIC, topic);
        intent.putExtra(LEVEL, level);
        startActivity(intent);
    }
}
