package com.android.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.popularmovies.R;
import com.android.popularmovies.model.GridItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<GridItem> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;
    private GridItem[] mGridData = null;
    private static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

    public GridViewAdapter(Context mContext, int layoutResourceId) {
        super(mContext, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    public GridViewAdapter(Context mContext, int layoutResourceId, GridItem[] mGridDatas) {
        super(mContext, layoutResourceId, mGridDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridDatas;
    }


    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(GridItem[] mGridData) {
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
            //holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData[position];
        //holder.titleTextView.setText(Html.fromHtml(item.getmTitle()));

        if (mGridData != null) {
            Picasso.with(mContext)
                    .load(POSTER_PATH.concat(mGridData[position].getmPoster()))
                    .fit()
                    .into(holder.imageView);
        }

        //Picasso.with(mContext).load(item.getmPoster()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}