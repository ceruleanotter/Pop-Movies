package io.github.ceruleanotter.popmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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


    public static ArrayList<PopMovie> getParsedMovieJSONData(String json) throws JSONException {
        ArrayList<PopMovie> listOfMovies = new ArrayList<PopMovie>();

        try {
            JSONObject moviesJson = new JSONObject(json);
            JSONArray results = moviesJson.getJSONArray(RESULTS_JSON);

            for(int i = 0 ; i < results.length(); i++ ) {
                JSONObject currentMovie = results.getJSONObject(i);
                String imageURL = currentMovie.getString(POSTER_IMAGE_JSON);
                imageURL = POSTER_BASE_URL + imageURL;
                listOfMovies.add(new PopMovie(imageURL));
            }

//            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//
//            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
//            String cityName = cityJson.getString(OWM_CITY_NAME);
//
//            JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
//            double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
//            double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

            return listOfMovies;
    }

}
