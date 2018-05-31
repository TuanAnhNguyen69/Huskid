package com.foodtiny.razor.elkid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.entity.EnglishWord;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by razor on 27/03/2018.
 */

public class AlphabetAdapter extends ArrayAdapter {
    private static final String letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private ArrayList<Character> characters;
    private Context context;
    private TextView character;
    private String word;
    private int id;
    private ArrayList<Integer> clickPosition;

    public AlphabetAdapter(@NonNull Context context) {
        super(context, R.layout.character);
        this.context = context;
        characters = new ArrayList<>();
        clickPosition = new ArrayList<>();
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

    private ArrayList<Character> generateLetter() {
        ArrayList<Character> letters = new ArrayList<>();
        for (char character : word.toCharArray()
             ) {
            letters.add(character);
        }

        Random random = new Random();
        for (int index = letters.size(); index < 14; index ++) {
            letters.add(letter.charAt(random.nextInt(letter.length())));
        }
        Collections.shuffle(letters);
        return letters;
    }

    public void setWord(String word) {
        this.word = word.toUpperCase();
        characters = generateLetter();
        this.clickPosition = new ArrayList<>();
    }

    public void addClickPosition(int clickPos) {
        clickPosition.add(clickPos);
    }

    public void removeLastClickPosition() {
        clickPosition.remove(clickPosition.size() - 1);
    }

    public int getLastClickPosition() {
        return clickPosition.get(clickPosition.size() - 1);
    }
}
