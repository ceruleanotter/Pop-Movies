package io.github.ceruleanotter.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lyla on 6/4/15.
 */
public class PopularMoviesAdapter extends BaseAdapter {
    private static final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();
    private Context mContext;



    private ArrayList<PopMovie> mData;/* =  new ArrayList<PopMovie>() {{
        add(new PopMovie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","intersetallar", "plot1",
                20,new Date(), 4.5, 4000));
        add(new PopMovie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","intersetallar", "plot2",
                20,new Date(), 4.5, 4000));
        add(new PopMovie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","intersetallar", "plot3",
                20,new Date(), 4.5, 4000));
        add(new PopMovie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","intersetallar", "plot4",
                20,new Date(), 4.5, 4000));
        add(new PopMovie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","intersetallar", "plot5",
                20,new Date(), 4.5, 4000));
        add(new PopMovie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","intersetallar", "plot5",
                20,new Date(), 4.5, 4000));
    }};*/


    public PopularMoviesAdapter(Context c) {
        super();
        mContext = c;
        //mData = null;
    }

    @Override
    public int getCount() {
        if (mData != null) return mData.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            GridView gv = (GridView) parent;
            GridView.LayoutParams params = new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT);
            /*params.width = (parent.getWidth()/gv.getNumColumns());
            params.height = (int)Math.round(params.width*1.5);
            imageView.setLayoutParams(params);*/
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        if (mData != null && position < mData.size()) {
            PopMovie currentData = mData.get(position);
            Picasso.with(mContext).load(currentData.getmImageURL()).into(imageView);
        } else {
            //THIS IS A PROBLEM
            Log.e(LOG_TAG, "mData is null or the position is strange");
        }

        //imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public void setmData(ArrayList<PopMovie> mData) {
        Log.e(LOG_TAG, "Set data has occurred");
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    public ArrayList<PopMovie> getmData() {
        return mData;
    }



}
