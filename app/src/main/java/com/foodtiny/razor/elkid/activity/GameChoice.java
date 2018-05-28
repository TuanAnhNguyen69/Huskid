package com.foodtiny.razor.elkid.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodtiny.razor.elkid.GlideApp;
import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.adapter.SlideAdapter;
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

public class GameChoice extends AppCompatActivity {


    @BindView(R.id.choice_bt1)
    FancyButton choice_bt1;

    @BindView(R.id.choice_bt2)
    FancyButton choice_bt2;

    @BindView(R.id.choice_bt3)
    FancyButton choice_bt3;

    @BindView(R.id.choice_bt4)
    FancyButton choice_bt4;

    @BindView(R.id.choice_image)
    ImageView image;

    @BindView(R.id.choice_scores)
    TextView choice_scores;

    @BindView(R.id.choice_exe)
    TextView choice_exe;

    SlideAdapter slideAdapter;
    List<EnglishWord> prilist;
    List<EnglishWord> uselist;
    String topic;
    static int questionCount = -1;
    int question = questionCount +1;
    int scores = 0;

    EnglishWord rightWord;
    EnglishWord wrongWord1;
    EnglishWord wrongWord2;
    EnglishWord wrongWord3;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_choice);
        ButterKnife.bind(this);



        topic = getIntent().getStringExtra(TOPIC);

        prilist = DatabaseHelper.getInstance().getTopicWords(topic);

        questionCount = -1;
        scores = 0;
        question = questionCount +1;

        uselist = new ArrayList<>();

        Random random = new Random();

        for (int i=0; i<10; i++){
            int rd = random.nextInt(prilist.size());
            uselist.add(prilist.get(rd));
            Log.d("aaa", ""+prilist.get(rd).getWord());
            prilist.remove(rd);
        }

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
                        questionCount = -1;
                        scores = 0;
                        question = questionCount +1;
                        prilist = DatabaseHelper.getInstance().getTopicWords(topic);
                        uselist = new ArrayList<>();
                        Random random = new Random();
                        for (int i=0; i<10; i++){
                            int rd = random.nextInt(prilist.size());
                            uselist.add(prilist.get(rd));
                            Log.d("aaa", ""+prilist.get(rd).getWord());
                            prilist.remove(rd);
                        }
                        playChoice();
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

        playChoice();

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
                SoundPlayer.getInstance().playMedia(GameChoice.this, resID);
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
                SoundPlayer.getInstance().playMedia(GameChoice.this, resID);
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
                SoundPlayer.getInstance().playMedia(GameChoice.this, resID);
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
                .singleShot(4)
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
                showcaseView.setShowcase(new ViewTarget(choice_scores), true);
                showcaseView.setContentTitle("This is your score");
                showcaseView.setContentText("");
                break;

            case 1:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showcaseView.setShowcase(new ViewTarget(choice_bt1), true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showcaseView.setShowcase(new ViewTarget(choice_bt2), true);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showcaseView.setShowcase(new ViewTarget(choice_bt3), true);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                showcaseView.setShowcase(new ViewTarget(choice_bt4), true);
                                            }
                                        }, 300);
                                    }
                                }, 300);
                            }
                        }, 300);

                    }
                }, 300);
                showcaseView.setContentTitle("Touch these button to chose result");
                showcaseView.setContentText("");
                break;

            case 2:
                showcaseView.setShowcase(new ViewTarget(choice_bt2), true);
                int resID = getResources().getIdentifier("_wrong", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(this, resID);
                choice_bt2.setBackgroundColor(Color.RED);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        choice_bt2.setBackgroundColor(Color.parseColor("#7ab800"));
                    }
                }, 1000);
                showcaseView.setContentTitle("If you select the wrong answer, that button will be red and noisy sound appear");
                showcaseView.setContentText("");
                break;

            case 3:
                showcaseView.setShowcase(new ViewTarget(choice_bt1), true);
                SoundPlayer.getInstance().pauseBackgroundMedia();
                int wordResID = getResources().getIdentifier("_" + choice_bt1.getText().toString().replaceAll(" ", "").toLowerCase(), "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(this, wordResID);
                choice_bt1.setBackgroundColor(Color.GREEN);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!storage.get(BG_SOUND).equals("OFF")) {
                            SoundPlayer.getInstance().playBackgroundMedia();
                        }
                        choice_bt1.setBackgroundColor(Color.parseColor("#7ab800"));
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


    public void playChoice(){
        questionCount++;

        if (questionCount > 9) {
            showDialog();
            return;
        }

        Random random = new Random();

        int pos = random.nextInt(prilist.size());
        wrongWord1 = prilist.get(pos);
        pos = random.nextInt(prilist.size());
        wrongWord2 = prilist.get(pos);
        pos = random.nextInt(prilist.size());
        wrongWord3 = prilist.get(pos);
        Log.d("ssas",""+ questionCount);

        if (uselist != null){
            rightWord = uselist.get(questionCount);
        }else{
            Log.d("ssas", "Sai");
        }

        if (uselist.get(questionCount).getImage() == null){
            Log.d("ssas", "Sai");
        }

        int wordResID = getResources().getIdentifier("_" + uselist.get(questionCount).getWord().replaceAll(" ", "").toLowerCase() + "_pic", "raw", getPackageName());
        GlideApp.with(this).load(wordResID).into(image);

        final int result = RandomButton(rightWord.getWord()
                ,wrongWord1.getWord()
                ,wrongWord2.getWord()
                ,wrongWord3.getWord());

        choice_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 1){
                    showCorrect(view);
                    return;

                }
                showWrong(result, view);
            }
        });

        choice_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 2){
                    showCorrect(view);
                    return;

                }
                showWrong(result, view);
            }
        });

        choice_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 3){
                    showCorrect(view);
                    return;
                }
                showWrong(result, view);
            }
        });

        choice_bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 4){
                    showCorrect(view);
                    return;
                }
                showWrong(result, view);
            }
        });

        choice_scores.setText(""+scores);
        choice_exe.setText(""+ question);

    }

    private void showWrong(int result, final View view) {
        int wordResID = getResources().getIdentifier("_wrong", "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, wordResID);
        final FancyButton button;
        switch (result) {
            case 1:
                button = choice_bt1;
                break;

            case 2:
                button = choice_bt2;
                break;

            case 3:
                button = choice_bt3;
                break;

            default:
                button = choice_bt4;
                break;
        }
        button.setBackgroundColor(Color.GREEN);
        view.setBackgroundColor(Color.RED);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                question++;
                button.setBackgroundColor(Color.parseColor("#7ab800"));
                view.setBackgroundColor(Color.parseColor("#7ab800"));
                playChoice();
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
                button.setBackgroundColor(Color.parseColor("#7ab800"));

                playChoice();
            }
        }, 3000);
        return;
    }

    public int RandomButton(String a, String b, String c, String d){

        int result;

        List<String> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);

        Random random = new Random();

        int pos = random.nextInt(4);
        choice_bt1.setText(list.get(pos));
        list.remove(pos);


        pos = random.nextInt(3);
        choice_bt2.setText(list.get(pos));
        list.remove(pos);

        pos = random.nextInt(2);
        choice_bt3.setText(list.get(pos));
        list.remove(pos);

        choice_bt4.setText(list.get(0));

        if(choice_bt1.getText().equals(a))
            return 1;
        else
            if(choice_bt2.getText().equals(a))
                return 2;
            else
                if (choice_bt3.getText().equals(a))
                    return 3;
                else
                    return 4;
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
