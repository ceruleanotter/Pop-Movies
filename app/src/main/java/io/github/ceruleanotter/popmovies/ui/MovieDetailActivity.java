package io.github.ceruleanotter.popmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vungle.publisher.VunglePub;

import io.github.ceruleanotter.popmovies.R;


public class MovieDetailActivity extends AppCompatActivity {

    // For Vungle
    final VunglePub vunglePub = VunglePub.getInstance();

    MovieDetailFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        //attaching the args if they were from an intent
        Bundle arguments = new Bundle();
        arguments.putInt(MovieDetailFragment.ID_BUNDLE_ARGS, getIntent().getIntExtra(MovieDetailFragment.ID_INTENT_EXTRA, -1));

        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_container, fragment)
                .commit();

        vunglePub.setEventListeners(fragment);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vunglePub.clearEventListeners();
    }
}
