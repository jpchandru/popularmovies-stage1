package com.android.popularmovies;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView plotSynopsisTextView;
    private ImageView imageView;
    private RatingBar ratingBarView;
    private FrameLayout frameLayout;
    private ColorDrawable colorDrawable;
    private static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting details screen layout
        setContentView(R.layout.activity_details_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //retrieves the thumbnail data
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String releaseDate = bundle.getString("releaseDate");
        String vote = bundle.getString("voteAverage");
        String synopsis = bundle.getString("plotSynopsis");
        String image = bundle.getString("image");
        //initialize and set the image description
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(Html.fromHtml(title));
        releaseDateTextView = (TextView) findViewById(R.id.releasedate);
        releaseDateTextView.setText(Html.fromHtml(releaseDate));
        ratingBarView = (RatingBar) findViewById(R.id.voteaverage);
        ratingBarView.setRating(Float.parseFloat(vote));
        plotSynopsisTextView = (TextView) findViewById(R.id.plotsynopsis);
        plotSynopsisTextView.setText(Html.fromHtml(synopsis));
        //Set image url
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        Picasso.with(this)
                .load(POSTER_PATH.concat(image))
                .fit()
                .into(imageView);
        //Set the background color to black
        frameLayout = (FrameLayout) findViewById(R.id.detail_background);
        colorDrawable = new ColorDrawable(Color.BLACK);
        frameLayout.setBackground(colorDrawable);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
