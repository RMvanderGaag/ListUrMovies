package com.avans.listurmovies.domain.movie;

import java.util.List;

public class MovieResults {
    private int page;
    private int total_results;
    private int total_pages;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<Movie> getResult() {
        return results;
    }
}
