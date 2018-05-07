package com.android.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.popularmovies.adapter.GridViewAdapter;
import com.android.popularmovies.model.GridItem;
import com.android.popularmovies.utilities.JsonUtils;
import com.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private GridItem[] mMovieData;

    private RelativeLayout relativeLayout;
    private ColorDrawable colorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        //mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);

                //Set the background color to black
                relativeLayout = (RelativeLayout) findViewById(R.id.activity_background);
                colorDrawable = new ColorDrawable(Color.BLACK);
                relativeLayout.setBackground(colorDrawable);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight()).
                        putExtra("title", item.getmTitle()).
                        putExtra("image", item.getmPoster()).
                        putExtra("releaseDate", item.getmReleaseDate()).
                        putExtra("voteAverage", String.valueOf(item.getmRating())).
                        putExtra("plotSynopsis", item.getmDescription());

                //Start details activity
                startActivity(intent);
            }
        });


        //Start download
        String movieQuery = "";
        URL movieSearchUrl = NetworkUtils.buildUrl(movieQuery);
        new AsyncHttpTask().execute(movieSearchUrl);
        mProgressBar.setVisibility(View.VISIBLE);
    }



    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<URL, Void, GridItem[]> {


        @Override
        protected GridItem[] doInBackground(URL... urls) {
            URL searchUrl = urls[0];

            String movieSearchResults;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHTTPUrl(searchUrl);
                mMovieData = JsonUtils.parseMovieJson(movieSearchResults);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieData;
        }



        @Override
        protected void onPostExecute(GridItem[] gridItems) {
            // Download complete. Lets update UI

            if (gridItems != null && gridItems.length > 0) {
                mGridAdapter = new GridViewAdapter(GridViewActivity.this, R.layout.grid_item_layout, gridItems);
                mGridAdapter.setGridData(gridItems);
                mGridView.setAdapter(mGridAdapter);
                mGridAdapter.notifyDataSetChanged();

            } else {
                System.out.println("Failed to Fetch Data --->"+gridItems.toString());
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }


}