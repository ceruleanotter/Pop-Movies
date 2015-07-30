package io.github.ceruleanotter.popmovies;

/**
 * Created by lyla on 6/5/15.
 */

import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.net.URL;

/**
 * Created by lyla on 6/4/15.
 */
public class SingleMovieLoader extends AsyncTaskLoader<PopMovie> {
    public final static String LOG_TAG = SingleMovieLoader.class.getSimpleName();
    //From this example http://stackoverflow.com/questions/20279216/asynctaskloader-basic-example-android
    PopMovie mData;
    int mId;


    public SingleMovieLoader(MovieDetailFragment df) {
        super(df.getActivity());
        mId = df.getmID();
    }

    @Override
    public PopMovie loadInBackground() {

        URL newMoviesUrl = MovieDataParsingUtilities.getUrlForNewMovies(getContext());
        Log.e(LOG_TAG, "Starting load for " + newMoviesUrl.toString());
        PopMovie toReturn = null;

        URL movieURL = MovieDataParsingUtilities.getUrlForSpecificMovie(mId);
        URL trailerURL = MovieDataParsingUtilities.getUrlForSpecificMovieTrailer(mId);

        String movieJSON = MovieDataParsingUtilities.getJSONFromWeb(movieURL);
        String trailerJSON = MovieDataParsingUtilities.getJSONFromWeb(trailerURL);

        toReturn = MovieDataParsingUtilities.movieFromJson(movieJSON, trailerJSON);


        return toReturn;
    }

    @Override
    public void deliverResult(final PopMovie data) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We don't need the result.
            if (data != null) {
                onReleaseResources(data);
            }
        }
        PopMovie oldData = mData;
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
    public void onCanceled(PopMovie data) {
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

    protected void onReleaseResources(PopMovie data) {

        //  nothing to do.
    }

}