package io.github.ceruleanotter.popmovies.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.vungle.publisher.AdConfig;
import com.vungle.publisher.VunglePub;

import io.github.ceruleanotter.popmovies.BuildConfig;
import io.github.ceruleanotter.popmovies.R;
import io.github.ceruleanotter.popmovies.utils.MovieDataParsingUtilities;


public class PopularMoviesActivity extends AppCompatActivity implements PopularMoviesFragment.MovieClickCallback,
        SharedPreferences.OnSharedPreferenceChangeListener {


    /**
     * Vungle related variables
     **/
    final VunglePub vunglePub = VunglePub.getInstance();


    String mCurrentSortBy;
    boolean mCurrentKidsMode;
    boolean mStarMode;
    boolean mTwoPane;
    boolean mFirstItemSelected;
    FrameLayout mMovieDetailContainerFrameLayout;

    final static String LOG_TAG = PopularMoviesActivity.class.getSimpleName();
    final static String MOVIEDETAILFRAGMENT_TAG = "MOVIE_DETAIL_FRAG_TAG";
    final static String BUNDLE_EXTRA_FIRST_ITEM_SELECTED = "FIRST_ITEM_SELECTED";


    private static boolean PREFERENCES_CHANGE_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PREFERENCES_CHANGE_FLAG = false;
        mCurrentSortBy = MovieDataParsingUtilities.getSortByPreference(this);
        mCurrentKidsMode = MovieDataParsingUtilities.getKidsModePreference(this);
        mStarMode = MovieDataParsingUtilities.getStarModePreference(this);
        Log.e(LOG_TAG, "Favorite mode is " + mStarMode);
        setContentView(R.layout.activity_popular_movies);

        mMovieDetailContainerFrameLayout = (FrameLayout) findViewById(R.id.movie_detail_container);
        if (findViewById(R.id.movie_detail_container) != null) {

            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), MOVIEDETAILFRAGMENT_TAG)
                        .commit();
            } else {
                mFirstItemSelected = savedInstanceState.getBoolean(BUNDLE_EXTRA_FIRST_ITEM_SELECTED, false);
                if (mFirstItemSelected) {
                    mMovieDetailContainerFrameLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            mTwoPane = false;
            //getSupportActionBar().setElevation(0f);
        }

        // Register preference change listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        //Vungle support
        // get your App ID from the app's main page on the Vungle Dashboard after setting up your app
        final String app_id = BuildConfig.VUNGLE_APP_ID;

        // initialize the Publisher SDK
        vunglePub.init(this, app_id);

        final AdConfig globalAdConfig = vunglePub.getGlobalAdConfig();
        globalAdConfig.setBackButtonImmediatelyEnabled(true);

        Log.e(LOG_TAG, "app id is " + app_id);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Register preference change listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PREFERENCES_CHANGE_FLAG) {
            mCurrentSortBy = MovieDataParsingUtilities.getSortByPreference(this);
            mCurrentKidsMode = MovieDataParsingUtilities.getKidsModePreference(this);
            mStarMode = MovieDataParsingUtilities.getStarModePreference(this);
            ((PopularMoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment)).onSettingsChange();
            PREFERENCES_CHANGE_FLAG = false;
        }

        // For Vungle
        vunglePub.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(BUNDLE_EXTRA_FIRST_ITEM_SELECTED, mFirstItemSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(int movieID) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putInt(MovieDetailFragment.ID_BUNDLE_ARGS, movieID);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            // Show the fragment now that it's been clicked on
            mMovieDetailContainerFrameLayout.setVisibility(View.VISIBLE);
            mFirstItemSelected = true;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIEDETAILFRAGMENT_TAG)
                    .commit();
        } else {

            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ID_INTENT_EXTRA,
                    movieID);
            startActivity(intent);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // If the preferences change, update the preferences and reload the data when you come back
        PREFERENCES_CHANGE_FLAG = true;
    }
}
