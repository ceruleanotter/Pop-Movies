package io.github.ceruleanotter.popmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lyla on 6/4/15.
 */
public class MovieDataParsingUtilities {
    public final static String LOG_TAG = MovieDataParsingUtilities.class.getSimpleName();





    // URL related constants
    //http://api.themoviedb.org/3/discover/movie?api_key=3062e696db60daf1cebee8178aa5f103
    final private static String MOVIE_BASE_URL =
            "http://api.themoviedb.org/3/discover/movie?";
    final private static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";

    final private static String API_KEY = "3062e696db60daf1cebee8178aa5f103";

    final private static String API_KEY_PARAM = "api_key";

    //JSON related constants
    final private static String RESULTS_JSON = "results";
    final private static String POSTER_IMAGE_JSON = "poster_path";
    final private static String TITLE_JSON = "original_title";
    final private static String PLOT_JSON = "overview";
    final private static String RUNTIME_JSON = "runtime";
    //this is only in the specific movie http://api.themoviedb.org/3/movie/245891?api_key=3062e696db60daf1cebee8178aa5f103
    final private static String RELEASE_DATE_JSON = "release_date";
    final private static String USER_RATING_JSON = "vote_average";
    final private static String ID_JSON = "id";




    public static URL getUrlForNewMovies(){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return url;
    }


    public static ArrayList<PopMovie> getParsedMovieDiscoveryJSONData(String json) throws JSONException {
        ArrayList<PopMovie> listOfMovies = new ArrayList<PopMovie>();

        try {
            JSONObject moviesJson = new JSONObject(json);
            JSONArray results = moviesJson.getJSONArray(RESULTS_JSON);

            for(int i = 0 ; i < results.length(); i++ ) {
                JSONObject currentMovie = results.getJSONObject(i);
                String title = currentMovie.getString(TITLE_JSON);
                String plot = currentMovie.getString(PLOT_JSON);

                double userRating = currentMovie.getDouble(USER_RATING_JSON);
                int id = currentMovie.getInt(ID_JSON);

                Date releaseDate = null;

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
                try {
                     releaseDate = formatter.parse(currentMovie.getString(RELEASE_DATE_JSON));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                String imageURL = currentMovie.getString(POSTER_IMAGE_JSON);
                imageURL = POSTER_BASE_URL + imageURL;

                listOfMovies.add(new PopMovie(imageURL, title, plot, releaseDate,userRating,id));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

            return listOfMovies;
    }

    public static void updateMovieFromJson(PopMovie movie, String json) {
        try {
            JSONObject movieJson = new JSONObject(json);
            String runtime = movieJson.getString(RUNTIME_JSON);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
