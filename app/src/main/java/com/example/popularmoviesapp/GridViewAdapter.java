package com.example.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Movie> mGridData = new ArrayList<Movie>();
    String base_url = "http://image.tmdb.org/t/p/w185";

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Movie> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<Movie> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Movie movie = mGridData.get(position);

        Picasso.get().load(base_url + movie.getPosterPath()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
