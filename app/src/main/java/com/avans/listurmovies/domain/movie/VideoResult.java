package com.avans.listurmovies.domain.movie;

import java.util.List;

public class VideoResult {
    private int movieId;
    private List<Video> results;

    public VideoResult(int movieId, List<Video> results) {
        this.movieId = movieId;
        this.results = results;
    }

    public int getMovieId() {
        return movieId;
    }

    public List<Video> getResults() {
        return results;
    }
}
