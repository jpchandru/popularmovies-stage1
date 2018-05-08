package com.android.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.android.popularmovies.R;
import com.android.popularmovies.model.MovieItem;
import com.squareup.picasso.Picasso;

public class MovieViewAdapter extends ArrayAdapter<MovieItem> {

    private Context mContext;
    private int layoutResourceId;
    private MovieItem[] mMovieData = null;
    private static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

    public MovieViewAdapter(Context mContext, int layoutResourceId) {
        super(mContext, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    public MovieViewAdapter(Context mContext, int layoutResourceId, MovieItem[] mMovieDatas) {
        super(mContext, layoutResourceId, mMovieDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mMovieData = mMovieDatas;
    }


    /**
     * Updates movie grid data and refresh movie grid items.
     *
     * @param mMovieData
     */
    public void setGridData(MovieItem[] mMovieData) {
        this.mMovieData = mMovieData;
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
            holder.imageView = (ImageView) row.findViewById(R.id.poster_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        if (mMovieData != null) {
            Picasso.with(mContext)
                    .load(POSTER_PATH.concat(mMovieData[position].getmPoster()))
                    .fit()
                    .into(holder.imageView);
        }
        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}