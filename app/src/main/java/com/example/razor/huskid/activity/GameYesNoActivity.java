package com.example.razor.huskid.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    @BindView(R.id.yn_scores)
    TextView yn_scores;

    @BindView(R.id.yn_bt_yes)
    Button yn_bt_yes;

    @BindView(R.id.yn_bt_no)
    Button yn_bt_no;

    SlideAdapter slideAdapter;
    EnglishWord currentWord;
    List<EnglishWord> prilist;
    List<EnglishWord> uselist;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_yes_no);
        ButterKnife.bind(this);

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
        yn_describe.setText("This is " + uselist.get(0).getWord());

        GlideApp.with(getApplicationContext()).load(uselist.get(0).getImage()).into(yn_image);



    }

    private void ThongBao(){
        Log.d("Thông báo","Đây là "+ uselist.get(0).getWord());
    }

}
