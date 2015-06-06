package io.github.ceruleanotter.popmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

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

        URL newMoviesUrl = MovieDataParsingUtilities.getUrlForNewMovies(getContext());
        Log.e(LOG_TAG, "Starting load for " + newMoviesUrl.toString());
        String moviesJSON = MovieDataParsingUtilities.getJSONFromWeb(newMoviesUrl);
        ArrayList<PopMovie> toReturn = null;
        try {
            toReturn = MovieDataParsingUtilities.getParsedMovieDiscoveryJSONData(moviesJSON);
            Log.e(LOG_TAG, "Size of new array list is  " + toReturn);
           /* for (int i = 0; i < toReturn.size(); i++) {
                PopMovie currentMovie = toReturn.get(i);
                int id = currentMovie.getmId();
                URL movieURL = MovieDataParsingUtilities.getUrlForSpecificMovie(id);
                Log.e(LOG_TAG, "Starting load for " + currentMovie.getmTitle() + "\n" + movieURL.toString());
                String movieJSON = MovieDataParsingUtilities.getJSONFromWeb(movieURL);
                MovieDataParsingUtilities.updateMovieFromJson(currentMovie,movieJSON);
            }*/
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return toReturn;
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
