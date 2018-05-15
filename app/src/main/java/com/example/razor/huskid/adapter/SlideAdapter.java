package com.example.razor.huskid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.razor.huskid.GlideApp;
import com.example.razor.huskid.R;
import com.example.razor.huskid.entity.EnglishWord;

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
//
//    @Override
//    public int getCount() {
//        return words.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return false;
//    }
//
//    @Nullable
//    @Override
//    public EnglishWord getItem(int position) {
//        return words.get(position);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        @SuppressLint("ViewHolder")
//        View rootView = LayoutInflater.from(context).inflate(R.layout.slider,parent, false);
//        ImageView imageView = rootView.findViewById(R.id.img_slider);
//        if (words.get(position) != null) {
//            GlideApp.with(context).load(words.get(position).getImage()).centerCrop().into(imageView);
//        }
//        return rootView;
//    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        @SuppressLint("ViewHolder")
        View rootView = LayoutInflater.from(context).inflate(R.layout.slider, container, false);
        ImageView imageView = rootView.findViewById(R.id.img_slider);
        if (words.get(position) != null) {
            GlideApp.with(context).load(words.get(position).getImage()).centerCrop().into(imageView);
        }
        container.addView(rootView);
        return rootView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
