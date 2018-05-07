package com.android.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.popularmovies.adapter.GridViewAdapter;
import com.android.popularmovies.model.GridItem;
import com.android.popularmovies.utilities.JsonUtils;
import com.android.popularmovies.utilities.MovieSorter;
import com.android.popularmovies.utilities.NetworkUtils;
import java.net.URL;

public class GridViewActivity extends AppCompatActivity {
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
                //Pass the movie data params to DetailsActivity
                intent.putExtra("title", item.getmTitle()).
                        putExtra("image", item.getmPoster()).
                        putExtra("releaseDate", item.getmReleaseDate()).
                        putExtra("voteAverage", String.valueOf(item.getmRating())).
                        putExtra("plotSynopsis", item.getmDescription());
                //Start details activity
                startActivity(intent);
            }
        });
        //Start download - default load are popular movies
        buildAndExecute(MovieSorter.popular.name());
    }

    //Downloading data asynchronously
    public class AsyncHttpTaskForMovieDataDownload extends AsyncTask<URL, Void, GridItem[]> {
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
                Toast.makeText(GridViewActivity.this, "Failed to retrieve data!", Toast.LENGTH_SHORT).show();
            }
            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movie_sorter, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_toprated) {
            buildAndExecute(MovieSorter.top_rated.name());
            return true;
        }else if (id == R.id.action_mostpopular) {
            buildAndExecute(MovieSorter.popular.name());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * build an URL and execute in  background
     * @param name
     */
    private void buildAndExecute(String name) {
        URL movieSearchUrl = NetworkUtils.buildUrl(name);
        new AsyncHttpTaskForMovieDataDownload().execute(movieSearchUrl);
        mProgressBar.setVisibility(View.VISIBLE);
    }
}