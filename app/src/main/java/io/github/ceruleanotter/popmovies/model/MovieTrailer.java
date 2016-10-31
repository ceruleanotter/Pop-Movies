package io.github.ceruleanotter.popmovies.model;

import android.net.Uri;

/**
 * Created by lyla on 7/30/15.
 */
public class MovieTrailer {
    private String mName;
    private Uri mUri;

    public MovieTrailer(String mName, Uri mUri) {
        this.mName = mName;
        this.mUri = mUri;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Uri getmUri() {
        return mUri;
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }
}
