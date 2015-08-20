package io.github.ceruleanotter.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MovieDetail extends AppCompatActivity {

    MovieDetailFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mFragment = (MovieDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

    }


}
