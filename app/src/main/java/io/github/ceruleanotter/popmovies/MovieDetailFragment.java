package io.github.ceruleanotter.popmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<PopMovie> {

    int mID;
    public final static String ID_EXTRA = "id_extra";
    private static final int MOVIE_LOADER = 1;
    @InjectView(R.id.title) TextView mTitleTextView;
    @InjectView(R.id.release_date) TextView mReleaseDateTextView;
    @InjectView(R.id.runtime) TextView mRunTimeTextView;
    @InjectView(R.id.plot) TextView mPlotTextView;
    @InjectView(R.id.poster) ImageView mPosterImageView;
    @InjectView(R.id.rating) RatingBar mRatingRatingBar;


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
        mTitleTextView.setText(data.getmTitle());
        mPlotTextView.setText(data.getmPlot());
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(data.getmReleaseDate());
        mReleaseDateTextView.setText(year);
        mRunTimeTextView.setText(data.getmRuntime()+"min");
        mRatingRatingBar.setRating((float)data.getmRating()/2);
        Picasso.with(getActivity()).load(data.getmImageURL()).into(mPosterImageView);
//        //mMovieTextView.setText("TITLE: " + data.getmTitle() +
//                "\n PLOT: " + data.getmPlot() );

    }

    @Override
    public void onLoaderReset(Loader<PopMovie> loader) {

    }
    
    public int getmID() {
        return mID;
    }
}
