package com.example.razor.huskid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.TopicsAdapter;
import com.example.razor.huskid.entity.Topic;
import com.example.razor.huskid.helper.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    public static final String TOPIC = "topic";

    @BindView(R.id.topicList)
    ListView topicListView;

    private TopicsAdapter topicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initTopicList();
    }

    private void initTopicList() {
        topicsAdapter = new TopicsAdapter(this, DatabaseHelper.getInstance().getAllTopics());
        topicListView.setAdapter(topicsAdapter);
        topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                intent.putExtra(TOPIC, DatabaseHelper.getInstance().getAllTopics().get(position).getName());
                startActivity(intent);
            }
        });
    }

    public void onButtonClick(View view, Topic topic) {
        RelativeLayout mainLayout = findViewById(R.id.activity_home_layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 20, 0);
    }


}
