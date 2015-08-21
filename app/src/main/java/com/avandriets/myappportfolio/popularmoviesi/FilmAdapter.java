package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FilmAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<FilmItem> mobileValues;

    public FilmAdapter(Context context, ArrayList<FilmItem> mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        FilmItem filmItem = (FilmItem) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_row, parent, false);
        }

        // Lookup view for data population
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.poster);
        Picasso.with(context).load(mobileValues.get(position).PosterURL).placeholder(R.drawable.ic_photo_black_48dp).error(R.drawable.ic_error_black_48dp).into(ivImage);

        return convertView;
    }

    @Override
    public int getCount() {
        return mobileValues.toArray().length;
    }

    @Override
    public Object getItem(int position) {
        return mobileValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}