package io.github.ceruleanotter.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public void onSettingsChange() {
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }
}
