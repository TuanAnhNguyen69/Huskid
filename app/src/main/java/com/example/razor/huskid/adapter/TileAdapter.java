package com.example.razor.huskid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.razor.huskid.R;
import com.example.razor.huskid.entity.Tile;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;

public class TileAdapter extends ArrayAdapter<Tile> {

    private Context context;
    private ArrayList<Tile> tiles;

    public TileAdapter(@NonNull Context context, @NonNull ArrayList<Tile> objects) {
        super(context, R.layout.tile, objects);
        this.context = context;
        this.tiles = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Tile getItem(int position) {
        return super.getItem(position);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            int width = getContext().getResources().getDisplayMetrics().widthPixels;
            GridView.LayoutParams params = new GridView.LayoutParams(width/12, width/12);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tile, parent, false);
            convertView.setLayoutParams(params);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tile tile = tiles.get(position);

        if (tile != null) {
            if (tile.getVerticalNumber() > 0 && tile.getHorizontalNumber() > 0) {
                viewHolder.character.setBackgroundResource(R.drawable.tile_both_bg);
            } else if (tile.getHorizontalNumber() > 0 || tile.getVerticalNumber() > 0) {
                viewHolder.character.setBackgroundResource(R.drawable.tile_un_show_bg);
            }

            if (tile.isShow()) {
                viewHolder.character.setText(tile.getCharacter().toString());
                viewHolder.character.setBackgroundResource(R.drawable.tile_un_show_bg);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.character)
        TextView character;

        @BindView(R.id.horizontalNumber)
        TextView horizontalNumber;

        @BindView(R.id.verticalNumber)
        TextView verticalNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
