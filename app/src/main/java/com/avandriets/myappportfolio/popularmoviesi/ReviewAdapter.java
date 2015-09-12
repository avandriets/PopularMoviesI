package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class ReviewAdapter extends ArrayAdapter<ReviewItem> {

    private Context context;
    private final ArrayList<ReviewItem> mobileValues;
    private int layoutId;

    public ReviewAdapter(Context context, int resourceLayoutId, ArrayList<ReviewItem> objects) {
        super(context, resourceLayoutId, objects);

        this.layoutId   = resourceLayoutId;
        this.context    = context;
        this.mobileValues    = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ReviewItem reviewItem = (ReviewItem) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        }

        // Lookup view for data population
        TextView tvAuthor = (TextView)convertView.findViewById(R.id.TextViewAuthor);
        TextView tvContent = (TextView)convertView.findViewById(R.id.TextViewContent);

        tvAuthor.setText(reviewItem.author);
        tvContent.setText(Html.fromHtml(reviewItem.content).toString());

        return convertView;
    }

    @Override
    public int getCount() {
        return mobileValues.toArray().length;
    }

    @Override
    public ReviewItem getItem(int position) {
        return mobileValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
