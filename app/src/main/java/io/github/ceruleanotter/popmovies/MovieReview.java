package io.github.ceruleanotter.popmovies;

import android.net.Uri;

/**
 * Created by lyla on 8/5/15.
 */
public class MovieReview {
    private String mReviewText;
    private Uri mUri;

    public MovieReview(String mReviewText, Uri mUri) {
        this.mReviewText = mReviewText;
        this.mUri = mUri;
    }

    public String getmReviewText() {
        return mReviewText;
    }

    public Uri getmUri() {
        return mUri;
    }

    public void setmReviewText(String mReviewText) {
        this.mReviewText = mReviewText;
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }
}
