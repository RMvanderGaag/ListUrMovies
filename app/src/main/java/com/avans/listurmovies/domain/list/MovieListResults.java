package com.avans.listurmovies.domain.list;

import java.util.List;

public class MovieListResults {

    private int page;
    private int total_results;
    private int total_pages;
    private List<MovieList> results;

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public List<MovieList> getResults() {
        return results;
    }
}
