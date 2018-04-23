package com.example.razor.huskid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.razor.huskid.GlideApp;
import com.example.razor.huskid.R;
import com.example.razor.huskid.entity.Topic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TopicsAdapter extends ArrayAdapter {

    private List<Topic> topics;
    private Context context;
    private static final ArrayList<Integer> colors = initColor();
    public TopicsAdapter(@NonNull Context context, @NonNull List<Topic> objects) {
        super(context, R.layout.activity_home_item, objects);
        topics = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return topics.size();
    }

    public static ArrayList<Integer> initColor() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(102, 153, 255));
        colors.add(Color.rgb(153, 102, 255));
        colors.add(Color.rgb(204, 102, 153));
        colors.add(Color.rgb(102, 102, 153));
        colors.add(Color.rgb(51, 153, 51));
        colors.add(Color.rgb(51, 204, 204));
        colors.add(Color.rgb(153, 255, 153));
        colors.add(Color.rgb(0, 51, 0));
        colors.add(Color.rgb(204, 102, 153));
        return colors;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_home_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        Topic topic = topics.get(position);
        if (topic != null) {
            Random random = new Random();
            viewHolder.backGround.setCardBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            viewHolder.topicImageView.setBorderColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            viewHolder.topicTextView.setText(topic.getName());
            GlideApp.with(getContext()).load(topic.getImage()).centerCrop().into(viewHolder.topicImageView);
        }
        return convertView;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    static class ViewHolder {
        @BindView(R.id.back_ground)
        CardView backGround;

        @BindView(R.id.topic_image)
        CircleImageView topicImageView;

        @BindView(R.id.topic)
        TextView topicTextView;

        @BindView(R.id.progress)
        ProgressBar topicProgressBar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
