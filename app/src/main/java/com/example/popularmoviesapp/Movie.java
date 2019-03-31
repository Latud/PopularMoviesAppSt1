package com.example.popularmoviesapp;

public class Movie{

    private Integer mId;
    private String mName;
    private String mPosterPath;
    private String mOverview;

    public Movie() {
        super();
    }

/*    public Movie(Integer id, String name, String posterPath, String overview) {
        mId = id;
        mName = name;
        mPosterPath = posterPath;
        mOverview = overview;
    }*/

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    }
