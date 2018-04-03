package com.example.razor.huskid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.razor.huskid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by razor on 27/03/2018.
 */

public class AlphabetAdapter extends ArrayAdapter {
    private static final String letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private ArrayList<Character> characters;
    private Context context;
    private TextView character;

    public AlphabetAdapter(@NonNull Context context) {
        super(context, R.layout.character);
        this.context = context;
        characters = new ArrayList<>();
        for (char character: letter.toCharArray()) {
            characters.add(character);
        }
    }

    @Override
    public int getCount() {
        return characters.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return characters.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.character, parent, false);
        }

        Character letter = characters.get(position);

        if (letter != null) {
            character = convertView.findViewById(R.id.letter);
            character.setText(letter.toString());
        }

        return convertView;
    }
}
