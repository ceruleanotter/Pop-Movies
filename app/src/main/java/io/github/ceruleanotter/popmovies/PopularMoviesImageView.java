package io.github.ceruleanotter.popmovies;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lyla on 6/4/15.
 */
public class PopularMoviesImageView extends ImageView {
    public PopularMoviesImageView(Context context) {
        super(context);
    }

    public PopularMoviesImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopularMoviesImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}
