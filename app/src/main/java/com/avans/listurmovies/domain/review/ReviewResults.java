package com.avans.listurmovies.domain.review;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResults {
    private int id;
    private int page;
    private int total_results;
    private int total_pages;
    private List<Review> results;

    public ReviewResults(int id, int page, int total_results, int total_pages, List<Review> reviews) {
        this.id = id;
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = reviews;
    }

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<Review> getReviews() {
        return results;
    }
}
