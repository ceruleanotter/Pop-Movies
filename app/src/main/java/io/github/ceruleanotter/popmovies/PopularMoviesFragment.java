package io.github.ceruleanotter.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<PopMovie>> {
    //Using ButterKnife's injection capabilities to get the view
    @InjectView(R.id.gridview_movies)
    GridView mGridView;
    PopularMoviesAdapter mMoviesAdapter;
    private static final int MOVIES_LOADER = 0;

    public PopularMoviesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        //For ButterKnife view injection
        ButterKnife.inject(this, rootView);

        mMoviesAdapter = new PopularMoviesAdapter(getActivity());
        final ViewTreeObserver vto = mGridView.getViewTreeObserver();
        // This code is from:
        // Added it because I was running in to an issue on rotation where I was getting super strange
        // behavior (views weren't appearing, things would get into an error state). I think it was
        // because the inital load time allowed for the grid view to be measured, whereas when
        // the screen was rotated, the gridview returned a height and width of 0 which really messed
        // up sizing calculations for the posters.

        // This code wait
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (mGridView.getWidth() > 10) { // because it may be called before the view is measured and you will still get 0
                    // here you can get the measured dimensions

                    mGridView.setAdapter(mMoviesAdapter);
                    // mMoviesAdapter.notifyDataSetChanged();
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {

                            Intent intent = new Intent(getActivity(), MovieDetail.class);
                            intent.putExtra(MovieDetailFragment.ID_EXTRA,
                                    mMoviesAdapter.getmData().get(position).getmId());

                            startActivity(intent);
                        }
                    });
                    ViewTreeObserver obs = mGridView.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this); // otherwise you're gonne keep getting called
                }
            }
        });


        return rootView;
    }


    @Override
    public Loader<ArrayList<PopMovie>> onCreateLoader(int id, Bundle args) {


        return new PopularMoviesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PopMovie>> loader, ArrayList<PopMovie> data) {
        if (data == null)
            return;
        mMoviesAdapter.setmData(data);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                /*String infos = "";
                PopMovie movie = mMoviesAdapter.getmData().get(position);
                infos = "Title: " + movie.getmTitle() + "\t runtime: " + movie.getmRuntime();
                Toast.makeText(getActivity(), infos,
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(getActivity(), MovieDetail.class);
                intent.putExtra(MovieDetailFragment.ID_EXTRA,
                        mMoviesAdapter.getmData().get(position).getmId());
                //.setData(contentUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PopMovie>> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
    }

    public void onSettingsChange() {
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }
}
