package io.github.ceruleanotter.popmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
    final private static String MOVIE_URL_BASE = "http://api.themoviedb.org/3";
    final private static String NEW_MOVIE_BASE_URL = MOVIE_URL_BASE + "/discover/movie?";
    final private static String SINGLE_MOVIE_BASE_URL = MOVIE_URL_BASE + "/movie/";
    final private static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";

    final private static String API_KEY = "3062e696db60daf1cebee8178aa5f103";

    final private static String API_KEY_PARAM = "api_key";
    final private static String SORT_BY_PARAM = "sort_by";
    final private static String CERT_COUNTRY_PARAM = "certification_country";
    final private static String CERTIFICATION_LEVEL_PARAM = "certification.lte";


    //public sort by options
    final public static String SORT_BY_POPULAR = "popularity.desc";
    final public static String SORT_BY_RELEASE = "release_date.desc";
    final public static String SORT_BY_RATING = "vote_average.desc";

    //kids mode settings
    final public static String KIDS_MODE_COUNTRY = "US";
    final public static String KIDS_MODE_LEVEL = "PG";

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


    public static URL getUrlForNewMovies(Context c) {


        String sortByParamValue;
        String sortBy = getSortByPreference(c);
        boolean kidsMode = getKidsModePreference(c);

        //wish there was some way to associate with the array in strings_activity_settings itself
        switch (sortBy) {
            case "0":
                sortByParamValue = SORT_BY_POPULAR;
                break;
            case "1":
                sortByParamValue = SORT_BY_RELEASE;
                break;
            case "2":
                sortByParamValue = SORT_BY_RATING;
                break;
            default:
                Log.e(LOG_TAG, "Shared pref returned something that wasn't 0, 1 or 2");
                sortByParamValue = SORT_BY_POPULAR;
                break;
        }

        Uri.Builder b = Uri.parse(NEW_MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(SORT_BY_PARAM, sortByParamValue);

        if (kidsMode) {
            b.appendQueryParameter(CERT_COUNTRY_PARAM, KIDS_MODE_COUNTRY)
                    .appendQueryParameter(CERTIFICATION_LEVEL_PARAM, KIDS_MODE_LEVEL);
        }

        Uri builtUri = b.build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return url;
    }


    public static URL getUrlForSpecificMovie(int id) {
        Uri builtUri = Uri.parse(SINGLE_MOVIE_BASE_URL + id).buildUpon()
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

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentMovie = results.getJSONObject(i);
                /*String title = currentMovie.getString(TITLE_JSON);
                String plot = currentMovie.getString(PLOT_JSON);

                double userRating = currentMovie.getDouble(USER_RATING_JSON);*/
                int id = currentMovie.getInt(ID_JSON);

               /* Date releaseDate = null;

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
                try {
                     releaseDate = formatter.parse(currentMovie.getString(RELEASE_DATE_JSON));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }*/

                String imageURL = currentMovie.getString(POSTER_IMAGE_JSON);
                imageURL = POSTER_BASE_URL + imageURL;

                listOfMovies.add(new PopMovie(id, imageURL));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return listOfMovies;
    }


    public static PopMovie movieFromJson(String movieJSON) {
        try {
            JSONObject currentMovie = new JSONObject(movieJSON);
            int runtime = currentMovie.getInt(RUNTIME_JSON);

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

            return new PopMovie(imageURL, title, plot, runtime, releaseDate, userRating, id);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }


    public static String getJSONFromWeb(URL url) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJSON = "";

        try {
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return "";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return "";
            }
            moviesJSON = buffer.toString();
            return moviesJSON;


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return "";
    }

    public static String getSortByPreference(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        String sortBy = sharedPref.getString(c.getString(R.string.pref_sort_by_key), "failsauce");
        return sortBy;
    }

    public static boolean getKidsModePreference(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        boolean kidsmode = sharedPref.getBoolean(c.getString(R.string.pref_kids_mode_key), false);
        return kidsmode;
    }


}
