package io.github.ceruleanotter.popmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<PopMovie> {

    int mID;
    public final static String ID_EXTRA = "id_extra";
    private static final int MOVIE_LOADER = 1;
    @InjectView(R.id.movie_text_view) TextView mMovieTextView;


    public MovieDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mID =getActivity().getIntent().getIntExtra(ID_EXTRA, -1);
        //For ButterKnife view injection
        ButterKnife.inject(this, rootView);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        return rootView;
    }

    @Override
    public Loader<PopMovie> onCreateLoader(int id, Bundle args) {
        return new SingleMovieLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<PopMovie> loader, PopMovie data) {
        if (data == null)
            return;
        mMovieTextView.setText("TITLE: " + data.getmTitle() +
                "\n PLOT: " + data.getmPlot() );

    }

    @Override
    public void onLoaderReset(Loader<PopMovie> loader) {

    }
    
    public int getmID() {
        return mID;
    }
}
