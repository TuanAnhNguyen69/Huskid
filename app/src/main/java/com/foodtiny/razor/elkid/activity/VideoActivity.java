package com.foodtiny.razor.elkid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.foodtiny.razor.elkid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodtiny.razor.elkid.activity.HomeActivity.TOPIC;

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.video)
    WebView video;

    String topic;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ButterKnife.bind(this);

        topic = getIntent().getStringExtra(TOPIC);

        video = new WebView(this);
        //video.setWebViewClient(new WebViewClient());
        url = "https://www.youtube.com/results?search_query=học+tiếng+anh+chủ+đề+" + topic;
        video.loadUrl(url);
        //Log.d("Xuat url",url + topic);
        setContentView(video);
    }
}
