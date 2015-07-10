package io.github.ceruleanotter.popmovies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lyla on 6/4/15.
 */
public class PopularMoviesAdapter extends BaseAdapter {
    private static final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;



    private ArrayList<PopMovie> mData;


    public PopularMoviesAdapter(Context c) {
        super();
        mContext = c;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

        final FrameLayout containerView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
             containerView = (FrameLayout)LayoutInflater.from(mContext).inflate(R.layout.item_movie_gridview, parent, false);

            final GridView gv = (GridView) parent;
            GridView.LayoutParams params = new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT);
            params.width = (gv.getWidth()/gv.getNumColumns());
            params.height = (int)Math.round(params.width*1.5);
            containerView.setLayoutParams(params);


        } else {
            containerView = (FrameLayout) convertView;
            containerView.setVisibility(View.INVISIBLE);
        }



        if (mData != null && position < mData.size()) {
            //ImageView imageView = new ImageView(mContext);
            final PopMovie currentData = mData.get(position);

            final ImageView imageView = (ImageView) containerView.findViewById(R.id.item_poster_imageview);

            Picasso.with(mContext).load(currentData.getmImageURL()).into(imageView, new Callback.EmptyCallback() {
               @Override
                public void onSuccess() {
                   containerView.setVisibility(View.VISIBLE);
                   imageView.setVisibility(View.VISIBLE);
                   TextView tv = (TextView) containerView.findViewById(R.id.item_title_textview);
                   tv.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    containerView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    TextView tv = (TextView) containerView.findViewById(R.id.item_title_textview);
                    tv.setText(currentData.getmTitle());
                    tv.setVisibility(View.VISIBLE);

                }
            });
        } else {
            //THIS IS A PROBLEM
            Log.e(LOG_TAG, "mData is null or the position is strange");
        }

        return containerView;
    }

    public void setmData(ArrayList<PopMovie> mData) {
        Log.e(LOG_TAG, "Set data has occurred");
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    public ArrayList<PopMovie> getmData() {
        return mData;
    }

    private void fadeInView(final View view, final boolean fadeIn) {
        float alphaVal = 0.0f;
        if (fadeIn) {
            alphaVal = 1.0f;
        }

        view.animate()
                .alpha(alphaVal)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (fadeIn) {
                            view.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.GONE);
                        }
                    }
                });
    }



}
