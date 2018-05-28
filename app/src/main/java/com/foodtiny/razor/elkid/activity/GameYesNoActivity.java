package com.foodtiny.razor.elkid.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodtiny.razor.elkid.GlideApp;
import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.entity.EnglishWord;
import com.foodtiny.razor.elkid.helper.DatabaseHelper;
import com.foodtiny.razor.elkid.helper.SoundPlayer;
import com.foodtiny.razor.elkid.helper.Storage;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.foodtiny.razor.elkid.activity.HomeActivity.BG_SOUND;
import static com.foodtiny.razor.elkid.activity.HomeActivity.TOPIC;

public class GameYesNoActivity extends AppCompatActivity {

    @BindView(R.id.yn_title)
    TextView title;

    @BindView(R.id.yn_imgage)
    ImageView image;

    @BindView(R.id.yn_describe)
    TextView describe;

    @BindView(R.id.yn_exe)
    TextView exeText;

    @BindView(R.id.yn_scores)
    TextView scoresText;

    @BindView(R.id.yn_bt_yes)
    FancyButton yes;

    @BindView(R.id.yn_bt_no)
    FancyButton no;

    private BlurPopupWindow winBuilder;
    private BlurPopupWindow exitBuilder;

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

    private ShowcaseView showcaseView;
    private int showCaseCount;

    TextView message;


    EnglishWord rightWord;
    EnglishWord wrongWord;
    List<EnglishWord> prilist;
    List<EnglishWord> uselist;
    String topic;
    static int questionCount = -1;
    int question = questionCount +1;
    int scores = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_yes_no);
        ButterKnife.bind(this);

        topic = getIntent().getStringExtra(TOPIC);

        prilist = DatabaseHelper.getInstance().getTopicWords(topic);
        uselist = new ArrayList<>();
        questionCount = -1;
        question = questionCount +1;
        scores = 0;
        Random random = new Random();

        for (int i=0; i<10; i++){
            int rd = random.nextInt(prilist.size());
            uselist.add(prilist.get(rd));
            Log.d("aaa", ""+prilist.get(rd).getWord());
            prilist.remove(rd);
        }

        YesOrNoPlay();

        exitBuilder = new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.popup_exit)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitBuilder.dismiss();
                    }
                }, R.id.no)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exit();
                    }
                }, R.id.yes)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();

        winBuilder = new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.popup_win)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prilist = DatabaseHelper.getInstance().getTopicWords(topic);
                        uselist = new ArrayList<>();
                        questionCount = -1;
                        question = questionCount +1;
                        scores = 0;
                        Random random = new Random();

                        for (int i=0; i<10; i++){
                            int rd = random.nextInt(prilist.size());
                            uselist.add(prilist.get(rd));
                            Log.d("aaa", ""+prilist.get(rd).getWord());
                            prilist.remove(rd);
                        }

                        YesOrNoPlay();
                        winBuilder.dismiss();
                    }
                }, R.id.again)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exit();
                    }
                }, R.id.exit)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();
        message = winBuilder.getContentView().findViewById(R.id.message);

        YesOrNoPlay();

        showCaseCount = 0;
        mute.setVisibility(View.INVISIBLE);
        about.setVisibility(View.INVISIBLE);
        settingOpen = false;
        storage = new Storage(this);
        settingLayout.setBackgroundResource(R.drawable.setting_back);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(GameYesNoActivity.this, resID);
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
                SoundPlayer.getInstance().playMedia(GameYesNoActivity.this, resID);
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
                SoundPlayer.getInstance().playMedia(GameYesNoActivity.this, resID);
                buildShowCase(image, "This is the image for choosing", "");
            }
        });

        buildFirstShowCase(image, "This is the image for choosing", "");
    }

    private void buildFirstShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        showcaseView = new ShowcaseView.Builder(this)
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
                .singleShot(5)
                .build();
        showcaseView.setButtonText("Next");
    }

    private void buildShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        showcaseView = new ShowcaseView.Builder(this)
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
        showcaseView.setShouldCentreText(true);
    }

    void nextShowCase() {
        final Handler handler = new Handler();
        switch (showCaseCount) {
            case 0:
                showcaseView.setShowcase(new ViewTarget(scoresText), true);
                showcaseView.setContentTitle("This is your score");
                showcaseView.setContentText("");
                break;

            case 1:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showcaseView.setShowcase(new ViewTarget(no), true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showcaseView.setShowcase(new ViewTarget(yes), true);
                            }
                        }, 300);
                    }
                }, 300);
                showcaseView.setContentTitle("Touch these button to chose result");
                showcaseView.setContentText("");
                break;

            case 2:
                showcaseView.setShowcase(new ViewTarget(no), true);
                int resID = getResources().getIdentifier("_wrong", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(this, resID);
                no.setBackgroundColor(Color.RED);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        no.setBackgroundColor(Color.parseColor("#cc2900"));
                    }
                }, 1000);
                showcaseView.setContentTitle("If you select the wrong answer, that button will be red and noisy sound appear");
                showcaseView.setContentText("");
                break;

            case 3:
                showcaseView.setShowcase(new ViewTarget(no), true);
                SoundPlayer.getInstance().pauseBackgroundMedia();
                int wordResID = getResources().getIdentifier("_" + rightWord.getWord().replaceAll(" ", "").toLowerCase(), "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(this, wordResID);
                no.setBackgroundColor(Color.GREEN);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!storage.get(BG_SOUND).equals("OFF")) {
                            SoundPlayer.getInstance().playBackgroundMedia();
                        }
                        no.setBackgroundColor(Color.parseColor("#cc2900"));
                    }
                }, 3000);
                showcaseView.setContentTitle("If you select the correct answer, that button will be green and word will be read");
                showcaseView.setContentText("");
                break;

            case 4:
                showcaseView.hide();
                showCaseCount = -1;
                showcaseView.setButtonText("Done");
                break;
        }
        showCaseCount++;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundPlayer.getInstance().pauseBackgroundMedia();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int resID = getResources().getIdentifier("_bgm3", "raw", getPackageName());
        SoundPlayer.getInstance().prepairBackgroundMedia(this, resID);
        if (!storage.get(BG_SOUND).equals("OFF")) {
            SoundPlayer.getInstance().playBackgroundMedia();
            storage.set(BG_SOUND, "ON");
            return;
        }

        mute.setImageResource(R.drawable.music_off);
    }

    @Override
    public void onBackPressed() {
        buildExitLayout();
    }

    public void exit() {
        if (exitBuilder.isShown()) {
            exitBuilder.dismiss();
        }

        if (winBuilder.isShown()) {
            winBuilder.dismiss();
        }
        finish();
    }

    public void buildExitLayout() {
        exitBuilder.show();
    }

    public void YesOrNoPlay(){

        Random random = new Random();
        int rd = random.nextInt(2);
        int rd_word = random.nextInt(prilist.size());

        questionCount++;

        Log.e("aaa", questionCount +"");
        if(questionCount >9){
            showDialog();
            return;
        }

        //Log.d("Xem uselist", "" + uselist.get(questionCount).getWord());
        //Log.d("Xem prilis", "" + prilist.get(rd_word).getWord());

        rightWord = uselist.get(questionCount);
        wrongWord = prilist.get(rd_word);

        if(rd != 0){
            describe.setText("This is " + wrongWord.getWord());

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showWrong(no, yes);
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCorrect(no);
                }
            });
        }
        else{
            describe.setText("This is " + rightWord.getWord());

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCorrect(yes);
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showWrong(yes, no);
                }
            });

        }
        int wordResID = getResources().getIdentifier("_" + uselist.get(questionCount).getWord().replaceAll(" ", "").toLowerCase() + "_pic", "raw", getPackageName());
        GlideApp.with(getApplicationContext()).load(wordResID).into(image);
        scoresText.setText(""+scores);
        exeText.setText(""+ question);
    }


    private void showWrong(final View correct, final View wrong) {
        int wordResID = getResources().getIdentifier("_wrong", "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, wordResID);
        correct.setBackgroundColor(Color.GREEN);
        wrong.setBackgroundColor(Color.RED);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                question++;
                yes.setBackgroundColor(Color.parseColor("#7ab800"));
                no.setBackgroundColor(Color.parseColor("#cc2900"));
                YesOrNoPlay();
            }
        }, 1000);
        return;
    }

    private void showCorrect(final View view) {
        SoundPlayer.getInstance().pauseBackgroundMedia();
        int wordResID = getResources().getIdentifier("_" + rightWord.getWord().replaceAll(" ", "").toLowerCase(), "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, wordResID);
        final FancyButton button = (FancyButton) view;
        button.setBackgroundColor(Color.GREEN);
        scores++;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                question++;
                if (!storage.get(BG_SOUND).equals("OFF")) {
                    SoundPlayer.getInstance().playBackgroundMedia();
                }
                yes.setBackgroundColor(Color.parseColor("#7ab800"));
                no.setBackgroundColor(Color.parseColor("#cc2900"));

                YesOrNoPlay();
            }
        }, 3000);
        return;
    }


    @SuppressLint("SetTextI18n")
    public void showDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Bạn được " + scores + " điểm");
//        builder.setMessage("Quay lại màn hình chính");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//                Intent intent = new Intent(GameChoice.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
        message.setText("You score " + Integer.toString(scores) + "/10");
        winBuilder.show();
    }

}
