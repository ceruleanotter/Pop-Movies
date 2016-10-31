package io.github.ceruleanotter.popmovies.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import io.github.ceruleanotter.popmovies.model.PopMovie;
import io.github.ceruleanotter.popmovies.ui.MovieDetailFragment;
import io.github.ceruleanotter.popmovies.utils.MovieDataParsingUtilities;

/**
 * Created by lyla on 6/4/15.
 */
public class PopularMoviesLoader extends AsyncTaskLoader<ArrayList<PopMovie>> {
    public final static String LOG_TAG = PopularMoviesLoader.class.getSimpleName();
    //From this example http://stackoverflow.com/questions/20279216/asynctaskloader-basic-example-android
    ArrayList<PopMovie> mData;
    Context mContext;


    public PopularMoviesLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public ArrayList<PopMovie> loadInBackground() {
        ArrayList<PopMovie> toReturn = null;
        boolean starMode = MovieDataParsingUtilities.getStarModePreference(mContext);



        if (starMode) {
            toReturn = new ArrayList<PopMovie>();
            //getting all of the url data from each starred movie
            SharedPreferences favorites = mContext.getSharedPreferences(MovieDetailFragment.STAR_DATASTORE, Context.MODE_PRIVATE);
            Map<String, ?> favoritesMap = favorites.getAll();
            for (Map.Entry<String, ?> movieStatus : favoritesMap.entrySet())
            {
                if (movieStatus.getValue() instanceof Boolean && movieStatus.getValue().equals(Boolean.TRUE)) {
                    URL currentURL = MovieDataParsingUtilities.getSingleMovieURLHelper(Integer.parseInt(movieStatus.getKey()),"");
                    String currentJSON = MovieDataParsingUtilities.getJSONFromWeb(currentURL);
                    PopMovie currentMovie = MovieDataParsingUtilities.movieFromJson(currentJSON);
                    toReturn.add(0, currentMovie);
                }
            }

        } else {
            URL newMoviesUrl = MovieDataParsingUtilities.getUrlForNewMovies(getContext());
            Log.e(LOG_TAG, "Starting load for " + newMoviesUrl.toString());
            String moviesJSON = MovieDataParsingUtilities.getJSONFromWeb(newMoviesUrl);

            try {
                toReturn = MovieDataParsingUtilities.getParsedMovieDiscoveryJSONData(moviesJSON);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }



        return toReturn;
    }

    @Override
    public void deliverResult(final ArrayList<PopMovie> data) {
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
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(ArrayList<PopMovie> data) {
        super.onCanceled(data);
        // At this point we can release the resources associated with 'data' if needed.
        onReleaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        // At this point we can release the resources associated with 'data' if needed.
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }

    protected void onReleaseResources(ArrayList<PopMovie> data) {
        //  nothing to do.
    }

}
