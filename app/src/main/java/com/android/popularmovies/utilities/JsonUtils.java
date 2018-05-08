package com.android.popularmovies.utilities;

import com.android.popularmovies.model.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    //Constants for the OptStrings for easier reference and updates
    private static String MOVIE_RESULTS = "results";
    private static String MOVIE_ID = "id";
    private static String MOVIE_RATING = "vote_average";
    private static String MOVIE_TITLE = "title";
    private static String MOVIE_POSTER = "poster_path";
    private static String MOVIE_OVERVIEW = "overview";
    private static String MOVIE_RELEASE_DATE = "release_date";


    public static MovieItem[] parseMovieJson(String json) throws JSONException {

        JSONObject movieDetails = new JSONObject(json);
        JSONArray moviesResults = movieDetails.getJSONArray("results");

        MovieItem[] movieArray = new MovieItem[moviesResults.length()];

        //loop through results array, create movie object and return in MovieArray
        for (int i = 0; i <moviesResults.length(); i++){
            MovieItem movie = new MovieItem();
            JSONObject movieData = moviesResults.getJSONObject(i);

            movieArray[i] = createMovieObject(movieData);
        }

        return movieArray;
    }

    private static MovieItem createMovieObject(JSONObject movieData){

        MovieItem movie = new MovieItem();

        //sets movie data points
        movie.setmId(movieData.optInt(MOVIE_ID));
        movie.setmRating(movieData.optInt(MOVIE_RATING));
        movie.setmTitle(movieData.optString(MOVIE_TITLE));
        movie.setmPoster(movieData.optString(MOVIE_POSTER));
        movie.setmDescription(movieData.optString(MOVIE_OVERVIEW));
        movie.setmReleaseDate(movieData.optString(MOVIE_RELEASE_DATE));

        return movie;

    }
}

