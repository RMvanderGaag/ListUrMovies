package com.avans.listurmovies.domain.movie;

public class RatingResponse {
    private int status_code;
    private String status_message;

    public RatingResponse(int status_code, String status_message) {
        this.status_code = status_code;
        this.status_message = status_message;
    }
}
