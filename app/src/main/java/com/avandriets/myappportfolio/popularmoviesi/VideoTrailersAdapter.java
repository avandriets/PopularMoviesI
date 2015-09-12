package com.avandriets.myappportfolio.popularmoviesi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class VideoTrailersAdapter extends ArrayAdapter<VideoItem> {

    private Context context;
    private final ArrayList<VideoItem> mobileValues;
    private int layoutId;

    public VideoTrailersAdapter(Context context, int resourceLayoutId, ArrayList<VideoItem> objects) {
        super(context, resourceLayoutId, objects);

        this.layoutId   = resourceLayoutId;
        this.context    = context;
        this.mobileValues    = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        VideoItem videoItem = (VideoItem) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        }

        // Lookup view for data population
        TextView tvTrailerName = (TextView)convertView.findViewById(R.id.TrailerNameTextView);
        tvTrailerName.setText(videoItem.name);

        return convertView;
    }

    @Override
    public int getCount() {
        return mobileValues.toArray().length;
    }

    @Override
    public VideoItem getItem(int position) {
        return mobileValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
