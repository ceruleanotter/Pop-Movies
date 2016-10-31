package io.github.ceruleanotter.popmovies.utils;

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
import java.util.Calendar;
import java.util.Date;

import io.github.ceruleanotter.popmovies.BuildConfig;
import io.github.ceruleanotter.popmovies.R;
import io.github.ceruleanotter.popmovies.model.MovieReview;
import io.github.ceruleanotter.popmovies.model.MovieTrailer;
import io.github.ceruleanotter.popmovies.model.PopMovie;

/**
 * Created by lyla on 6/4/15.
 */
public class MovieDataParsingUtilities {
    public final static String LOG_TAG = MovieDataParsingUtilities.class.getSimpleName();
    // URL related constants
    //http://api.themoviedb.org/3/discover/movie?api_key=xxx
    final private static String MOVIE_URL_BASE = "http://api.themoviedb.org/3";
    final private static String NEW_MOVIE_BASE_URL = MOVIE_URL_BASE + "/discover/movie?";
    final private static String SINGLE_MOVIE_BASE_URL = MOVIE_URL_BASE + "/movie/";
    final private static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    final private static String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

    final private static String TRAILER_PATH_TO_APPEND_URL = "/videos";
    final private static String REVIEW_PATH_TO_APPEND_URL = "/reviews";

    final private static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    final private static String API_KEY_PARAM = "api_key";
    final private static String SORT_BY_PARAM = "sort_by";
    final private static String CERT_COUNTRY_PARAM = "certification_country";
    final private static String CERTIFICATION_LEVEL_PARAM = "certification.lte";
    final private static String RELEASE_DATE_PARAM = "release_date.lte";

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
    final private static String BACKDROP_IMAGE_JSON = "backdrop_path";
    final private static String TITLE_JSON = "original_title";
    final private static String PLOT_JSON = "overview";
    final private static String RUNTIME_JSON = "runtime";
    final private static String ID_JSON = "id";

    //this is only in the specific movie http://api.themoviedb.org/3/movie/245891?api_key=xxx
    final private static String RELEASE_DATE_JSON = "release_date";
    final private static String USER_RATING_JSON = "vote_average";

    //JSON trailer and review constants
    final private static String TRAILER_RESULT_JSON = "results";
    private static final String URL_YOUTUBE = "https://www.youtube.com/watch";
    private static final String TRAILER_SITE_JSON = "site";
    private static final String TRAILER_YOUTUBE_VALUE_JSON = "YouTube";
    private static final String YOUTUBE_ID_PARAM = "v";
    private static final String TRAILER_YOUTUBE_KEY_JSON = "key";
    private static final String TRAILER_NAME_JSON = "name";

    final private static String REVIEW_RESULT_JSON = "results";
    final private static String REVIEW_ID_JSON = "id";
    final private static String REVIEW_CONTENT_JSON = "content";

    final private static String URL_SINGLE_REVIEW = "https://www.themoviedb.org/review";
    final private static String REVIEW_LANG_PARAM_KEY = "language";
    final private static String REVIEW_LANG_PARAM_VALUE_ENGLISH = "en";


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
                Log.e(LOG_TAG, "Shared pref returned something that wasn't 0, 1 or 2, it was " + sortBy);
                sortByParamValue = SORT_BY_POPULAR;
                break;
        }
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        Uri.Builder b = Uri.parse(NEW_MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(SORT_BY_PARAM, sortByParamValue)
                .appendQueryParameter(RELEASE_DATE_PARAM, sdf.format(today));

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
        return getSingleMovieURLHelper(id, "");
    }

    public static URL getUrlForSpecificMovieTrailer(int id) {
        return getSingleMovieURLHelper(id, TRAILER_PATH_TO_APPEND_URL);
    }

    public static URL getUrlForSpecificMovieReviews(int id) {
        return getSingleMovieURLHelper(id, REVIEW_PATH_TO_APPEND_URL);
    }

    public static URL getSingleMovieURLHelper(int id, String extraPath) {
        Uri builtUri = Uri.parse(SINGLE_MOVIE_BASE_URL + id + extraPath).buildUpon()
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
                String title = currentMovie.getString(TITLE_JSON);

                int id = currentMovie.getInt(ID_JSON);

                String imageURL = currentMovie.getString(POSTER_IMAGE_JSON);
                imageURL = POSTER_BASE_URL + imageURL;

                listOfMovies.add(new PopMovie(id, imageURL, title));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return listOfMovies;
    }

    public static PopMovie movieFromJson(String movieJSON) {
        return movieFromJson(movieJSON, null, null);
    }

    public static PopMovie movieFromJson(String movieJSON, String trailerJSON, String reviewJSON) {

        String backdropURL = "null";
        String imageURL = "null";
        Date releaseDate = null;
        double userRating = 0;
        int id = -1;
        int runtime = 0;
        String title = "No title provided.";
        String plot = "No overview provided.";


        try {
            JSONObject currentMovie = new JSONObject(movieJSON);

            id = currentMovie.getInt(ID_JSON);
            title = currentMovie.getString(TITLE_JSON);
            plot = currentMovie.getString(PLOT_JSON);
            imageURL = currentMovie.getString(POSTER_IMAGE_JSON);
            imageURL = POSTER_BASE_URL + imageURL;

            backdropURL = currentMovie.getString(BACKDROP_IMAGE_JSON);
            backdropURL = BACKDROP_BASE_URL + backdropURL;

            userRating = currentMovie.getDouble(USER_RATING_JSON);
            runtime = currentMovie.getInt(RUNTIME_JSON);

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
            try {
                releaseDate = formatter.parse(currentMovie.getString(RELEASE_DATE_JSON));
            } catch (ParseException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        ArrayList<MovieTrailer> trailers = (trailerJSON == null) ? null : trailerUriListFromJson(trailerJSON);
        ArrayList<MovieReview> reviews = (reviewJSON == null) ? null : reviewUriListFromJson(reviewJSON);

        return new PopMovie(backdropURL, imageURL, title, plot, runtime, releaseDate, userRating, id, trailers, reviews);
    }

    public static ArrayList<MovieTrailer> trailerUriListFromJson(String trailerJSON) {

        ArrayList<MovieTrailer> trailers = new ArrayList<MovieTrailer>();
        try {
            JSONArray listOfTrailers = new JSONObject(trailerJSON).getJSONArray(TRAILER_RESULT_JSON);
            for (int i = 0; i < listOfTrailers.length(); i++) {
                JSONObject currentTrailer = listOfTrailers.getJSONObject(i);
                if (currentTrailer.getString(TRAILER_SITE_JSON).equals(TRAILER_YOUTUBE_VALUE_JSON)) {
                    trailers.add(new MovieTrailer(currentTrailer.getString(TRAILER_NAME_JSON),
                            Uri.parse(URL_YOUTUBE).buildUpon().appendQueryParameter(
                                    YOUTUBE_ID_PARAM, currentTrailer.getString(TRAILER_YOUTUBE_KEY_JSON))
                                    .build()));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return trailers;
    }

    public static ArrayList<MovieReview> reviewUriListFromJson(String reviewJSON) {
        ArrayList<MovieReview> reviews = new ArrayList<MovieReview>();
        try {
            JSONArray listOfReviews = new JSONObject(reviewJSON).getJSONArray(REVIEW_RESULT_JSON);
            for (int i = 0; i < listOfReviews.length(); i++) {


                JSONObject currentReview = listOfReviews.getJSONObject(i);

                reviews.add(new MovieReview(currentReview.getString(REVIEW_CONTENT_JSON),
                        Uri.parse(String.format(URL_SINGLE_REVIEW)).buildUpon()
                                .appendPath(currentReview.getString(REVIEW_ID_JSON))
                                .appendQueryParameter(REVIEW_LANG_PARAM_KEY, REVIEW_LANG_PARAM_VALUE_ENGLISH)
                                .build()));

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return reviews;
    }

    public static String getJSONFromWeb(URL url) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJSON = "";

        try {
            // Create the request to MoviesDb, and open the connection
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
                return "";
            }
            moviesJSON = buffer.toString();
            return moviesJSON;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the data, there's no point in attempting
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

    public static boolean getStarModePreference(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(c.getString(R.string.pref_star_mode_key), false);
    }
}
