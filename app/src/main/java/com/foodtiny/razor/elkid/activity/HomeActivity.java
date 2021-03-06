package com.foodtiny.razor.elkid.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.adapter.TopicsAdapter;
import com.foodtiny.razor.elkid.helper.DatabaseHelper;
import com.foodtiny.razor.elkid.helper.SoundPlayer;
import com.foodtiny.razor.elkid.helper.Storage;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    public static final String TOPIC = "topic";
    public static final String LEVEL = "level";
    public static final String BG_SOUND = "bgsound";

//    @BindView(R.id.topicList)
//    ListView topicListView;

    @BindView(R.id.topicGrid)
    GridView topicGridView;

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
    boolean learnTypeOpen;
    Storage storage;
    BlurPopupWindow popup;
    ShowcaseView showcaseView;
    private int showCaseCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_grid);
        ButterKnife.bind(this);
        storage = new Storage(this);
        initTopicList();
        settingOpen = false;
        showCaseCount = 0;
        gameLevelOpen = false;
        settingLayout.setBackgroundResource(R.drawable.setting_back);
        mute.setVisibility(View.INVISIBLE);
        about.setVisibility(View.INVISIBLE);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(HomeActivity.this, resID);
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
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(HomeActivity.this, resID);
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
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(HomeActivity.this, resID);
                //buildShowCase(topicListView, "Touch here to open topic menu", "");
                buildShowCase(topicGridView, "Touch here to open topic menu", "");
            }
        });

        //buildFirstShowCase(topicListView, "Touch here to open topic menu", "");
        buildFirstShowCase(topicGridView, "Touch here to open topic menu", "");
    }

    private void buildFirstShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        showcaseView= new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextShowCase();
                    }
                })
                .blockAllTouches()
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(1)
                .build();
        showcaseView.setButtonText("Next");
    }

    private void nextShowCase() {
        switch (showCaseCount) {
            case 0:
                showcaseView.setShowcase(new ViewTarget(settingLayout), true);
                showcaseView.setContentTitle("Touch to open setting");
                showcaseView.setContentText("");
                break;

            case 1:
                settingLayout.setBackgroundResource(R.drawable.setting_back_full);
                mute.setVisibility(View.VISIBLE);
                about.setVisibility(View.VISIBLE);
                settingOpen =true;
                showcaseView.setShowcase(new ViewTarget(mute), true);
                showcaseView.setContentTitle("Touch to toggle back ground music");
                showcaseView.setContentText("");
                break;

            case 2:
                showcaseView.setShowcase(new ViewTarget(about), true);
                showcaseView.setContentTitle("Touch to show this any time you want");
                showcaseView.setContentText("");
                break;

            case 3:
                settingLayout.setBackgroundResource(R.drawable.setting_back);
                mute.setVisibility(View.INVISIBLE);
                about.setVisibility(View.INVISIBLE);
                settingOpen = false;
                showcaseView.hide();
                showCaseCount = -1;
                showcaseView.setButtonText("Done");

                break;
        }
        showCaseCount++;
    }

    private void buildShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        showcaseView= new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .blockAllTouches()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextShowCase();
                    }
                })
                .setStyle(R.style.CustomShowcaseTheme)
                .build();
        showcaseView.setButtonText("Next");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundPlayer.getInstance().pauseBackgroundMedia();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int resID = getResources().getIdentifier("_bgm1", "raw", getPackageName());
        SoundPlayer.getInstance().prepairBackgroundMedia(this, resID);
        if (!storage.get(BG_SOUND).equals("OFF")) {
            SoundPlayer.getInstance().playBackgroundMedia();
            storage.set(BG_SOUND, "ON");
            return;
        }

        mute.setImageResource(R.drawable.music_off);
    }

    private void initTopicList() {
        topicsAdapter = new TopicsAdapter(this, DatabaseHelper.getInstance().getAllTopics());
//        topicListView.setAdapter(topicsAdapter);
//        topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                onButtonClick(topicsAdapter.getItem(position).getName());
//            }
//        });
        topicGridView.setAdapter(topicsAdapter);
        topicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onButtonClick(topicsAdapter.getItem(position).getName());
            }
        });
    }

    public void onButtonClick(final String topic) {
        int resID = getResources().getIdentifier("_click", "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, resID);
        BlurPopupWindow builder = new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.popup_topic)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        View gameMode = v.getRootView().findViewById(R.id.learn_mode);
                        View learnLayout = v.getRootView().findViewById(R.id.popup_play_layout);
                        if (learnTypeOpen) {
                            gameMode.setVisibility(View.GONE);
                            learnLayout.setVisibility(View.VISIBLE);
                            learnTypeOpen = false;
                            return;
                        }
                        gameMode.setVisibility(View.VISIBLE);
                        learnLayout.setVisibility(View.GONE);
                        learnTypeOpen = true;
                    }
                }, R.id.btnLearn)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        gotoLearn(topic);
                    }
                }, R.id.flash_card)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        gotoVideo(topic);
                    }
                }, R.id.video)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        View gameMode = v.getRootView().findViewById(R.id.game_mode);
                        View hard = v.getRootView().findViewById(R.id.cross);
                        View very_hard = v.getRootView().findViewById(R.id.drag);
                        View learnLayout = v.getRootView().findViewById(R.id.popup_learn_layout);
                        if (gameLevelOpen) {
                            gameMode.setVisibility(View.GONE);
                            learnLayout.setVisibility(View.VISIBLE);
                            gameLevelOpen = false;
                            return;
                        }
                        if(topic.equals("simple sentence")){
                            hard.setVisibility(View.GONE);
                            very_hard.setVisibility(View.GONE);
                        }
                        gameMode.setVisibility(View.VISIBLE);
                        learnLayout.setVisibility(View.GONE);
                        gameLevelOpen = true;
                    }
                }, R.id.btnPlay)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        gotoYesNoGame(topic);
                    }
                }, R.id.yes_no)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        gotoChoiceGame(topic);
                    }
                }, R.id.choice)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        gotoCrossWordGame(topic, 10);
                    }
                }, R.id.cross)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = getResources().getIdentifier("_click", "raw", v.getContext().getPackageName());
                        SoundPlayer.getInstance().playMedia(v.getContext(), resID);
                        gotoDragDrop(topic);
                    }
                }, R.id.drag)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();
        this.popup = builder;
        builder.show();
    }

    public void gotoLearn(String topic) {
        Intent intent = new Intent(HomeActivity.this, LearnActivity.class);
                intent.putExtra(TOPIC, topic);
                startActivity(intent);
                popup.dismiss();
    }

    public void gotoVideo(String topic) {
        Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
        intent.putExtra(TOPIC, topic);
        startActivity(intent);
        popup.dismiss();
    }

    public void gotoYesNoGame(String topic){
        Intent intent = new Intent(HomeActivity.this, GameYesNoActivity.class);
                intent.putExtra(TOPIC, topic);
                startActivity(intent);
        popup.dismiss();
    }

    public void gotoChoiceGame(String topic){
        Intent intent = new Intent(HomeActivity.this, GameChoice.class);
        intent.putExtra(TOPIC, topic);
        startActivity(intent);
        popup.dismiss();
    }

    public void gotoCrossWordGame(String topic, int level) {
        Intent intent = new Intent(HomeActivity.this, CrossWordGameActivity.class);
        intent.putExtra(TOPIC, topic);
        intent.putExtra(LEVEL, level);
        startActivity(intent);
        popup.dismiss();
    }

    public void gotoDragDrop(String topic){
        Intent intent = new Intent(HomeActivity.this, DragDropActivity.class);
        intent.putExtra(TOPIC, topic);
        startActivity(intent);
        popup.dismiss();
    }
}
