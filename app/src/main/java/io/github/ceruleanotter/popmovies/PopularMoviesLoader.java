package io.github.ceruleanotter.popmovies;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lyla on 6/4/15.
 */
public class PopularMoviesLoader extends AsyncTaskLoader<ArrayList<PopMovie>> {
    public final static String LOG_TAG = PopularMoviesLoader.class.getSimpleName();
    //From this example http://stackoverflow.com/questions/20279216/asynctaskloader-basic-example-android
    ArrayList<PopMovie> mData;


    public PopularMoviesLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<PopMovie> loadInBackground() {

        URL movieUrl = MovieDataParsingUtilities.getUrlForNewMovies();
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJSON = "";


        try {
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) movieUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
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
                return null;
            }
            moviesJSON = buffer.toString();
            ArrayList<PopMovie> toReturn = MovieDataParsingUtilities.getParsedMovieJSONData(moviesJSON);
            return toReturn;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
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
        return null;
    }


    @Override
    public void deliverResult(final ArrayList<PopMovie> data) {
        //Log.d("AppLog", "deliverResult");
        if (isReset()) {
            // An async query came in while the loader is stopped.  We don't need the result.
            if (data != null) {
                onReleaseResources(data);
            }
        }
        ArrayList<PopMovie> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(data);
        }

        // At this point we can release the resources associated with
        // 'oldData' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldData != null) {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        //        Log.d("AppLog", "onStartLoading");
        super.onStartLoading();
        if (mData != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mData);
        }
        // check if config changed
        //            boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

        if (takeContentChanged() || mData == null) {// || configChange) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        //        Log.d("AppLog", "onStopLoading");
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(ArrayList<PopMovie> data) {
        super.onCanceled(data);
        //        Log.d("AppLog", "onCanceled:" + data);
        // At this point we can release the resources associated with 'data' if needed.
        onReleaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        //        Log.d("AppLog", "onReset");
        // Ensure the loader is stopped
        onStopLoading();
        // At this point we can release the resources associated with 'data' if needed.
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }

    protected void onReleaseResources(ArrayList<PopMovie> data) {
        //        Log.d("AppLog", "onReleaseResources");
        //  nothing to do.
    }


}
