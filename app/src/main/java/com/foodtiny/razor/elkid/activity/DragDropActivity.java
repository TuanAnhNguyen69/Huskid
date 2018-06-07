package com.foodtiny.razor.elkid.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.entity.EnglishWord;
import com.foodtiny.razor.elkid.helper.DatabaseHelper;
import com.foodtiny.razor.elkid.helper.SoundPlayer;
import com.foodtiny.razor.elkid.helper.Storage;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.foodtiny.razor.elkid.activity.HomeActivity.BG_SOUND;
import static com.foodtiny.razor.elkid.activity.HomeActivity.TOPIC;

public class DragDropActivity extends AppCompatActivity {

    @BindView(R.id.dd_reset)
    FancyButton bt_reset;

    @BindView(R.id.dd_check)
    FancyButton bt_check;

    @BindView(R.id.layout_drag_drop)
    LinearLayout main_layout;

    @BindView(R.id.dd_scores)
    TextView tx_scores;

    @BindView(R.id.dd_exe)
    TextView tx_excecise;

    @BindView(R.id.dd_alphabet_1)
    LinearLayout subLayout_Alphabet_1;

    @BindView(R.id.dd_alphabet_2)
    LinearLayout subLayout_Alphabet_2;

    @BindView(R.id.dd_input_1)
    LinearLayout subLayout_Input_1;

    @BindView(R.id.dd_input_2)
    LinearLayout subLayout_Input_2;

    @BindView(R.id.setting)
    ImageView setting;

    @BindView(R.id.mute)
    ImageView mute;

    @BindView(R.id.about)
    ImageView about;

    @BindView(R.id.setting_layout)
    ConstraintLayout settingLayout;

    String topic;
    List<EnglishWord> prilist;
    List<EnglishWord> uselist;

    static int questionCount = -1;
    int question = questionCount +1;
    int scores = 0;

    private BlurPopupWindow winBuilder;
    private BlurPopupWindow exitBuilder;

    Storage storage;
    TextView message;

    String subInput1;
    String subInput2;

//    List<LinearLayout> layout_alphabet;
//    List<LinearLayout> layout_input;

    List<TextView> tx_input_1;
    List<TextView> tx_input_2;

    List<TextView> tx_alphabet_1;
    List<TextView> tx_alphabet_2;

//    LinearLayout subLayout_Input_1;
//    LinearLayout subLayout_Input_2;
//    LinearLayout subLayout_Alphabet_1;
//    LinearLayout subLayout_Alphabet_2;

    EnglishWord word;

    boolean settingOpen;

    private ShowcaseView showcaseView;
    private int showCaseCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_2);

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
                SoundPlayer.getInstance().playMedia(DragDropActivity.this, resID);
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
                SoundPlayer.getInstance().playMedia(DragDropActivity.this, resID);
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



//        subLayout_Input_1 = new LinearLayout(this);
//        subLayout_Input_2 = new LinearLayout(this);
//        subLayout_Alphabet_1 = new LinearLayout(this);
//        subLayout_Alphabet_2 = new LinearLayout(this);
//
//        subLayout_Input_1.setGravity(Gravity.CENTER);
//        subLayout_Input_2.setGravity(Gravity.CENTER);
//        subLayout_Alphabet_1.setGravity(Gravity.CENTER);
//        subLayout_Alphabet_2.setGravity(Gravity.CENTER);
//
//        main_layout.addView(subLayout_Alphabet_1);
//        main_layout.addView(subLayout_Alphabet_2);
//        main_layout.addView(subLayout_Input_1);
//        main_layout.addView(subLayout_Input_2);

        tx_input_1 = new ArrayList<>();
        tx_input_2 = new ArrayList<>();

        tx_alphabet_1 = new ArrayList<>();
        tx_alphabet_2 = new ArrayList<>();

        PlayDrag();

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

                        PlayDrag();
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
    }

    public void PlayDrag(){
        questionCount++;

        tx_scores.setText(scores + "");
        tx_excecise.setText(question + "");

        if (questionCount > 9) {
            showDialog();
            return;
        }

        subLayout_Input_1.removeAllViews();
        subLayout_Input_2.removeAllViews();

        subLayout_Alphabet_1.removeAllViews();
        subLayout_Alphabet_2.removeAllViews();

        tx_input_1.removeAll(tx_input_1);
        tx_input_2.removeAll(tx_input_2);

        tx_alphabet_1.removeAll(tx_alphabet_1);
        tx_alphabet_2.removeAll(tx_alphabet_2);


//        tx_input_1.removeAll(tx_input_1);
//        tx_input_2.removeAll(tx_input_2);
//
//        tx_alphabet_1.removeAll(tx_alphabet_1);
//        tx_alphabet_2.removeAll(tx_alphabet_2);

        LinearLayout.LayoutParams marginInputBox = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        marginInputBox.setMargins(3, 10, 3, 10);

        Drawable dr1 = getBaseContext().getResources().getDrawable(R.drawable.drag_item);
        Drawable drag_in = getBaseContext().getResources().getDrawable(R.drawable.drag_in);
        Drawable drag_out = getBaseContext().getResources().getDrawable(R.drawable.drag_out);

        word = uselist.get(questionCount);
        String string = word.getWord();
        Log.d("bbb", string);

        subInput2 ="";

        for(int i=0; i<string.length(); i++){
            if(Character.toString(string.charAt(i)).matches( " ")){
                subInput2 = string.substring(i+1);
                subInput1 = string.substring(0, i);
                break;
            }
            else {
                subInput1 = string;
            }
        }

        Log.d("sub1", subInput1+"");
        Log.d("sub2", subInput2+"");

        String mixAlphabet;
        if(subInput2 != null){
            mixAlphabet = subInput1.concat(subInput2);
        }else{
            mixAlphabet = subInput1;
        }

        mixAlphabet = MixString(mixAlphabet);

        //Rendering alphabet boxs
        for(int i=0; i<=mixAlphabet.length()/2; i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setText(mixAlphabet.charAt(i)+"");
            textView.setBackground(dr1);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setLayoutParams(marginInputBox);
            tx_alphabet_1.add(textView);
            subLayout_Alphabet_1.addView(textView);
        }
        for(int i=mixAlphabet.length()/2+1; i<mixAlphabet.length(); i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setText(mixAlphabet.charAt(i)+"");
            textView.setBackground(dr1);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setLayoutParams(marginInputBox);
            tx_alphabet_2.add(textView);
            subLayout_Alphabet_2.addView(textView);
        }


        //Rendering input boxs
        for (int i=0; i<subInput1.length(); i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(drag_out);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setLayoutParams(marginInputBox);
            tx_input_1.add(textView);
            subLayout_Input_1.addView(textView);
        }

        if(subInput2 != null) {
            for (int i = 0; i < subInput2.length(); i++) {
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(drag_out);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setLayoutParams(marginInputBox);
                tx_input_2.add(textView);
                subLayout_Input_2.addView(textView);
            }
        }

        for(int i=0; i<tx_alphabet_1.size(); i++){
            tx_alphabet_1.get(i).setOnTouchListener(new MyTouch());
        }

        for(int i=0; i<tx_alphabet_2.size(); i++){
            tx_alphabet_2.get(i).setOnTouchListener(new MyTouch());
        }

        for(int i=0; i<tx_input_1.size(); i++){

            tx_input_1.get(i).setOnDragListener(new MyDrag());
        }


        if(subInput2 !=null ){
            for(int i=0; i<tx_input_2.size(); i++){
                tx_input_2.get(i).setOnDragListener(new MyDrag());
            }
        }

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionCount--;
                PlayDrag();
            }
        });

        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String temp = "";
//                for(int i=0; i< tx_input_1.size(); i++){
//                    temp = temp + (tx_input_1.get(i).getText()+"");
//                }
//                Log.d("list1", temp+ "");
//
//                String temp2 = "";
//                for(int i=0; i< tx_input_2.size(); i++){
//                    temp = temp + (tx_input_2.get(i).getText()+"");
//                }
//                Log.d("list2", temp2+ "");

//                Log.d("Lỗi", subInput1+"");
//                Log.d("Lỗi", subInput2+"");
                if(subInput2 != ""){
                    if(Check(subInput1, tx_input_1) == true && Check(subInput2, tx_input_1) == true){
                        Log.d("Kết quả", "Đúng rồi");
                        showCorrect();
                    }
                    else{
                        Log.d("Kết quả", "Sai rồi");
                        showWrong();
                    }
                }
                else {
                    if(Check(subInput1, tx_input_1) == true){
                        Log.d("Kết quả", "Đúng rồi");
                        showCorrect();
                    }
                    else{
                        Log.d("Kết quả", "Sai rồi");
                        showWrong();
                    }
                }

            }
        });
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
/*
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
*/
    public boolean Check(String str, List<TextView> list){
        String temp = "";
        for(int i=0; i< list.size(); i++){
            temp = temp + (list.get(i).getText()+"");
        }
        Log.d("list", temp+ "");
        if(str.equals(temp))
            return true;
        else
            return false;
    }

    public String MixString(String str){
        int m = str.length();
        if(m>0){
            String result = "";
            List<String> list = new ArrayList();

            for (int i=0; i<m; i++){
                list.add((str.charAt(i) + ""));
            }

            Collections.shuffle(list);


            for (int i=0; i<m; i++){
                result = result+list.get(i)+"";
            }
            return result;
        }else
            return str;
    }

    @SuppressLint("SetTextI18n")
    public void showDialog(){
        message.setText("You score " + scores + "/10");
        winBuilder.show();
    }

    private final class MyTouch implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                ClipData data = ClipData.newPlainText("value",((TextView)view).getText());
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                view.startDrag(data, shadowBuilder, view, 0);

                view.setVisibility(View.VISIBLE);

                return true;
            }else
                return false;
        }
    }

    private final class MyDrag implements View.OnDragListener{
        Drawable drag_in = getResources().getDrawable(R.drawable.drag_in);
        Drawable drag_out = getResources().getDrawable(R.drawable.drag_out);

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            View newView = (View) dragEvent.getLocalState();
            LinearLayout owner = (LinearLayout) newView.getParent();
            TextView container = (TextView) view;
            switch (dragEvent.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackground(drag_in);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackground(drag_out);
                    break;
                case DragEvent.ACTION_DROP:
                    if(container.getText()==""){
                        newView.setVisibility(View.INVISIBLE);
                        String str = dragEvent.getClipData().getItemAt(0).getText().toString();
                        ((TextView) view).setText(str + "");
                    }else{
                        owner.removeView(newView);
                        owner.addView(newView);
                        newView.setVisibility(View.VISIBLE);
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.setBackground(drag_out);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private void showWrong() {
        int wordResID = getResources().getIdentifier("_wrong", "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, wordResID);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                question++;
                PlayDrag();
            }
        }, 1000);
        return;
    }

    private void showCorrect() {
        SoundPlayer.getInstance().pauseBackgroundMedia();
        int wordResID = getResources().getIdentifier("_" + word.getId(), "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, wordResID);
        scores++;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                question++;
                PlayDrag();
            }
        }, 3000);
        return;
    }
}
