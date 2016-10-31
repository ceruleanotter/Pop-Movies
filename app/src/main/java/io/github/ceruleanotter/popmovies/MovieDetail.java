package io.github.ceruleanotter.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MovieDetail extends AppCompatActivity {

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
    }

}
