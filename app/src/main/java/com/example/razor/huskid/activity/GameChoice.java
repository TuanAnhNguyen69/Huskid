package com.example.razor.huskid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.razor.huskid.GlideApp;
import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.SlideAdapter;
import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.helper.DatabaseHelper;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.razor.huskid.activity.HomeActivity.TOPIC;

public class GameChoice extends AppCompatActivity {


    @BindView(R.id.choice_bt1)
    Button choice_bt1;

    @BindView(R.id.choice_bt2)
    Button choice_bt2;

    @BindView(R.id.choice_bt3)
    Button choice_bt3;

    @BindView(R.id.choice_bt4)
    Button choice_bt4;

    @BindView(R.id.choice_imgage)
    ImageView choice_image;

    @BindView(R.id.choice_scores)
    TextView choice_scores;

    @BindView(R.id.choice_exe)
    TextView choice_exe;

    SlideAdapter slideAdapter;
    List<EnglishWord> prilist;
    List<EnglishWord> uselist;
    String topic;
    static int x = -1;
    int exececise = x+1;
    int scores = 0;

    EnglishWord rightWord;
    EnglishWord wrongWord1;
    EnglishWord wrongWord2;
    EnglishWord wrongWord3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_choice);
        ButterKnife.bind(this);

        x = -1;
        scores = 0;
        exececise = x +1;

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




        PlayChoice();
    }

    public void PlayChoice(){

        x++;
        if(x>9){
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
        Log.d("ssas",""+ x);

        if (uselist != null){
            rightWord = uselist.get(x);
        }else{
            Log.d("ssas", "Sai");
        }

        if (uselist.get(x).getImage() == null){
            Log.d("ssas", "Sai");
        }
        GlideApp.with(this).load(uselist.get(x).getImage()).into(choice_image);

        final int result = RandomButton(rightWord.getWord()
                ,wrongWord1.getWord()
                ,wrongWord2.getWord()
                ,wrongWord3.getWord());

        choice_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 1){
                    scores++;
                }
                exececise++;
                PlayChoice();
            }
        });

        choice_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 2){
                    scores++;
                }
                exececise++;
                PlayChoice();
            }
        });

        choice_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 3){
                    scores++;
                }
                exececise++;
                PlayChoice();
            }
        });

        choice_bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == 4){
                    scores++;
                }
                exececise++;
                PlayChoice();
            }
        });

        choice_scores.setText(""+scores);
        choice_exe.setText(""+exececise);

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

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bạn được " + scores + " điểm");
        builder.setMessage("Quay lại màn hình chính");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(GameChoice.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
