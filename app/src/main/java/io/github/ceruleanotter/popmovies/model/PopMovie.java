package io.github.ceruleanotter.popmovies.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lyla on 6/4/15.
 */
public class PopMovie {

    private String mImageURL;
    private String mBackdropURL;

    private String mTitle;
    private String mPlot;

    private int mRuntime;
    private Date mReleaseDate;
    private double mRating;
    private int mId;

    private ArrayList<MovieTrailer> mTrailers;
    private ArrayList<MovieReview> mReviews;

    public PopMovie(String mBackdropURL, String mImageURL, String mTitle, String mPlot, int mRuntime,
                    Date mReleaseDate, double mRating, int mId, ArrayList<MovieTrailer> mTrailers,
                    ArrayList<MovieReview> mReviews) {
        this.mBackdropURL = mBackdropURL;
        this.mImageURL = mImageURL;
        this.mTitle = mTitle;
        this.mPlot = mPlot;
        this.mRuntime = mRuntime;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
        this.mId = mId;
        this.mTrailers = mTrailers;
        this.mReviews = mReviews;
    }

    public PopMovie(int mId, String mImageURL, String title) {
        this.mId = mId;
        this.mImageURL = mImageURL;
        this.mTitle = title;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPlot() {
        return mPlot;
    }

    public int getmRuntime() {
        return mRuntime;
    }

    public Date getmReleaseDate() {
        return mReleaseDate;
    }

    public double getmRating() {
        return mRating;
    }

    public int getmId() {
        return mId;
    }

    public void setmRuntime(int mRuntime) {
        this.mRuntime = mRuntime;
    }

    public String getmBackdropURL() {
        return mBackdropURL;
    }

    public ArrayList<MovieTrailer> getmTrailers() {
        return mTrailers;
    }

    public ArrayList<MovieReview> getmReviews() {
        return mReviews;
    }
}
