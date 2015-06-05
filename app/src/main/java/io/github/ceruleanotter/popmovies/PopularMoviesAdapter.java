package io.github.ceruleanotter.popmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lyla on 6/4/15.
 */
public class PopularMoviesAdapter extends BaseAdapter {
    private Context mContext;



    private ArrayList<PopMovie> mData;


    public PopularMoviesAdapter(Context c) {
        mContext = c;
        mData = null;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
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


        PopularMoviesImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new PopularMoviesImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (PopularMoviesImageView) convertView;
        }

        if (mData != null && position < mData.size()) {
            PopMovie currentData = mData.get(position);
            Picasso.with(mContext).load(currentData.getmImageURL()).into(imageView);
        } else {
            //THIS IS A PROBLEM
        }

        //imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public void setmData(ArrayList<PopMovie> mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    public ArrayList<PopMovie> getmData() {
        return mData;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}
