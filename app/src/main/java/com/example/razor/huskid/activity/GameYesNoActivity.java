package com.example.razor.huskid.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razor.huskid.GlideApp;
import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.SlideAdapter;
import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.helper.DatabaseHelper;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.razor.huskid.activity.HomeActivity.TOPIC;

public class GameYesNoActivity extends AppCompatActivity {

    @BindView(R.id.yn_title)
    TextView yn_title;

    @BindView(R.id.yn_imgage)
    ImageView yn_image;

    @BindView(R.id.yn_describe)
    TextView yn_describe;

    @BindView(R.id.yn_exe)
    TextView yn_exe;

    @BindView(R.id.yn_scores)
    TextView yn_scores;

    @BindView(R.id.yn_bt_yes)
    Button yn_bt_yes;

    @BindView(R.id.yn_bt_no)
    Button yn_bt_no;


    SlideAdapter slideAdapter;
    EnglishWord rightWord;
    EnglishWord wrongWord;
    List<EnglishWord> prilist;
    List<EnglishWord> uselist;
    String topic;
    static int x = -1;
    int exececise = x+1;
    int scores = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_yes_no);
        ButterKnife.bind(this);
        x = -1;
        exececise = x+1;
        scores = 0;




        topic = getIntent().getStringExtra(TOPIC);

        prilist = DatabaseHelper.getInstance().getTopicWords(topic);
        slideAdapter = new SlideAdapter(this, prilist);
        uselist = new ArrayList<>();
        slideAdapter = new SlideAdapter(this, uselist);

        Random random = new Random();

        for (int i=0; i<10; i++){
            int rd = random.nextInt(prilist.size());
            uselist.add(prilist.get(rd));
            Log.d("aaa", ""+prilist.get(rd).getWord());
            prilist.remove(rd);
        }






        YesOrNoPlay();
        /*
        if(x>9){
            Toast.makeText(this, "Chúc mừng bạn", Toast.LENGTH_SHORT).show();
            //showDialog();
        }
        */
        /*
        yn_bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yn_title.setText("Yes");
                YesOrNoPlay();
            }
        });

        yn_bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yn_title.setText("No");
                YesOrNoPlay();
            }
        });
        */

    }

    private void ThongBao(){
        Log.d("Thông báo","Đây là "+ uselist.get(0).getWord());
    }

    public void YesOrNoPlay(){

        Random random = new Random();
        int rd = random.nextInt(2);
        int rd_word = random.nextInt(prilist.size());

        x++;

        Log.e("aaa", x+"");
        if(x>9){
            showDialog();
            return;
        }

        //Log.d("Xem uselist", "" + uselist.get(x).getWord());
        //Log.d("Xem prilis", "" + prilist.get(rd_word).getWord());

        rightWord = uselist.get(x);
        wrongWord = prilist.get(rd_word);

        if(rd != 0){
            yn_describe.setText("This is " + wrongWord.getWord());

            yn_bt_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exececise ++;
                    YesOrNoPlay();
                }
            });

            yn_bt_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exececise ++;
                    scores ++;
                    YesOrNoPlay();
                }
            });
        }
        else{
            yn_describe.setText("This is " + rightWord.getWord());

            yn_bt_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exececise ++;
                    scores ++;
                    YesOrNoPlay();
                }
            });

            yn_bt_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exececise ++;
                    YesOrNoPlay();
                }
            });

        }
        int wordResID = getResources().getIdentifier("_" + uselist.get(x).getWord().replaceAll(" ", "").toLowerCase() + "_pic", "raw", getPackageName());
        GlideApp.with(getApplicationContext()).load(wordResID).into(yn_image);
        yn_scores.setText(""+scores);
        yn_exe.setText(""+exececise);
    }



    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bạn được " + scores + " điểm");
        builder.setMessage("Quay lại màn hình chính");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(GameYesNoActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
