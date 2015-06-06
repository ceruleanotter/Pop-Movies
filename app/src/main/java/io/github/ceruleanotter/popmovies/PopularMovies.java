package io.github.ceruleanotter.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class PopularMovies extends AppCompatActivity {

    String mCurrentSortBy;
    boolean mCurrentKidsMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentSortBy = MovieDataParsingUtilities.getSortByPreference(this);
        mCurrentKidsMode = MovieDataParsingUtilities.getKidsModePreference(this);
        setContentView(R.layout.activity_popular_movies);
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
    protected void onResume() {
        super.onResume();
        String sortByNow = MovieDataParsingUtilities.getSortByPreference(this);
        boolean kidsByNow = MovieDataParsingUtilities.getKidsModePreference(this);
        if (!(sortByNow.equals(mCurrentSortBy)) || !(kidsByNow == mCurrentKidsMode)) {
            ((PopularMoviesFragment)getSupportFragmentManager().findFragmentById(R.id.fragment)).onSettingsChange();
            mCurrentSortBy = sortByNow;
        }
    }
}
