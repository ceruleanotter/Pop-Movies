package io.github.ceruleanotter.popmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<PopMovie>> {
    //Using ButterKnife's injection capabilities to get the view
    @InjectView(R.id.gridview_movies) GridView mGridView;
    PopularMoviesAdapter mMoviesAdapter;
    private static final int MOVIES_LOADER = 0;

    public PopularMoviesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_popular_movies, container, false);

        //For ButterKnife view injection
        ButterKnife.inject(this, rootView);

        mMoviesAdapter = new PopularMoviesAdapter(getActivity());
        mGridView.setAdapter(mMoviesAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);

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
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PopMovie>> loader) {

    }
}
