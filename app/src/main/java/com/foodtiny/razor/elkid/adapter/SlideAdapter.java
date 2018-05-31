package com.foodtiny.razor.elkid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.foodtiny.razor.elkid.GlideApp;
import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.entity.EnglishWord;

import java.util.ArrayList;
import java.util.List;

public class SlideAdapter extends PagerAdapter {

    private List<EnglishWord> words;
    private Context context;

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    public SlideAdapter(@NonNull Context context, @NonNull List<EnglishWord> objects) {
        super();
        this.context = context;
        words = objects;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        @SuppressLint("ViewHolder")
        View rootView = LayoutInflater.from(context).inflate(R.layout.slider, container, false);
        ImageView imageView = rootView.findViewById(R.id.img_slider);

        if (words.get(position) != null) {
            int resID = context.getResources().getIdentifier("_" + words.get(position).getId() + "_pic", "raw", context.getPackageName());
            //int resID = context.getResources().getIdentifier("_" + words.get(position).getWord().replaceAll(" ", "").toLowerCase() + "_pic", "raw", context.getPackageName());
            GlideApp.with(context).load(resID).fitCenter().into(imageView);
        }
        container.addView(rootView);

        return rootView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
