package io.github.ceruleanotter.popmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
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
    @InjectView(R.id.title)
    TextView mTitleTextView;
    @InjectView(R.id.release_date)
    TextView mReleaseDateTextView;
    @InjectView(R.id.runtime)
    TextView mRunTimeTextView;
    @InjectView(R.id.plot)
    TextView mPlotTextView;
    @InjectView(R.id.poster)
    ImageView mPosterImageView;
    @InjectView(R.id.backdrop)
    ImageView mBackdropImageView;
    @InjectView(R.id.rating)
    RatingBar mRatingRatingBar;

    @InjectView(R.id.star)
    RatingBar mStar;

    @InjectView(R.id.titlebar)
    RelativeLayout mTitleBarLayout;



    @InjectView(R.id.root_trailers_linear_layout)
    LinearLayout mRootLayoutForTrailers;

    @InjectView(R.id.root_reviews_linear_layout)
    LinearLayout mRootLayoutForReviews;

    public MovieDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mID = getActivity().getIntent().getIntExtra(ID_EXTRA, -1);
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
        if (data.getmId() == -1)
            return;
        mTitleTextView.setText(data.getmTitle());

        mPlotTextView.setText(data.getmPlot());

        String year = "Unknown year";


        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            year = df.format(data.getmReleaseDate());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mReleaseDateTextView.setText(year);
        mRunTimeTextView.setText(data.getmRuntime() + "min");
        mRatingRatingBar.setRating((float) data.getmRating() / 2);
        Picasso.with(getActivity()).load(data.getmImageURL()).into(mPosterImageView);
        mBackdropImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(getActivity()).load(data.getmBackdropURL()).into(mBackdropImageView);



        //Add trailers
        LayoutInflater lf = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        for (MovieTrailer t : data.getmTrailers()) {
            LinearLayout currentMovieTrailerLayout = (LinearLayout)lf.inflate(R.layout.item_trailer, null);
            ((TextView)currentMovieTrailerLayout.findViewById(R.id.trailer_name)).setText(t.getmName());
            final Uri currentLink = t.getmUri();
            ((Button) currentMovieTrailerLayout.findViewById(R.id.trailer_play_button)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, currentLink ));

                        }
                    }
            );

            mRootLayoutForTrailers.addView(currentMovieTrailerLayout);


        }

        //Add reviews
        for (MovieReview r : data.getmReviews()) {
            LinearLayout currentMovieReviewLayout = (LinearLayout)lf.inflate(R.layout.item_review, null);
            ((TextView)currentMovieReviewLayout.findViewById(R.id.review_summary)).setText(r.getmReviewText());
            final Uri currentLink = r.getmUri();
            Button b = (Button) currentMovieReviewLayout.findViewById(R.id.read_review_button);
            b.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, currentLink));

                        }
                    }
            );


            mRootLayoutForReviews.addView(currentMovieReviewLayout);
        }

        Picasso.with(getActivity())
                .load(data.getmBackdropURL())
                .fit().centerCrop()
                .transform(PaletteTransformation.instance())
                .into(mBackdropImageView, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        //Required for grabbing the palette color as per: http://jakewharton.com/coercing-picasso-to-play-with-palette/
                        Bitmap bitmap = ((BitmapDrawable) mBackdropImageView.getDrawable()).getBitmap(); // Ew!
                        Palette palette = PaletteTransformation.getPalette(bitmap);
                        Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();
                        if (vibrantSwatch != null) {
                            float[] vibrant = vibrantSwatch.getHsl();
                            int color = Color.HSVToColor(vibrant);
                            mTitleBarLayout.setBackgroundColor(color);
                            setButtonColors(color);

                        }

                    }

                    @Override
                    public void onError() {
                        mBackdropImageView.setVisibility(View.GONE);
                    }
                });
    }

    private void setButtonColors(int color) {

        for(int i = 0; i < mRootLayoutForTrailers.getChildCount(); i++) {
            LinearLayout currentItem = (LinearLayout)mRootLayoutForTrailers.getChildAt(i);
            Button b = (Button)currentItem.findViewById(R.id.trailer_play_button);
            b.setBackgroundColor(color);
        }
        for(int i = 0; i < mRootLayoutForReviews.getChildCount(); i++) {
            LinearLayout currentItem = (LinearLayout)mRootLayoutForReviews.getChildAt(i);
            Button b = (Button)currentItem.findViewById(R.id.read_review_button);
            b.setBackgroundColor(color);
        }

    }

    @Override
    public void onLoaderReset(Loader<PopMovie> loader) {

    }

    public int getmID() {
        return mID;
    }


}