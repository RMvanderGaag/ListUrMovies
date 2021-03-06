package com.avans.listurmovies.domain.movie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
    //General
    private int id;
    private String title;
    private String overview;

    private Date release_date;
    @SerializedName("genre_ids")
    private int[] genres;

    //Original
    private String original_language;
    private String original_title;

    //Images
    private String poster_path;
    private String backdrop_path;

    //Votes/Popularity
    private double popularity;
    private double vote_average;
    private int vote_count;

    //Other
    private boolean adult;

    public Movie(int id, String title, String overview, Date release_date, int[] genres, String original_language, String original_title,
                 String poster_path, String backdrop_path, double popularity, double vote_average, int vote_count, boolean adult) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.genres = genres;
        this.original_language = original_language;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.adult = adult;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public int[] getGenres() {
        return genres;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public boolean isAdult() {
        return adult;
    }
}
