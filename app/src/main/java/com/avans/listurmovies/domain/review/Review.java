package com.avans.listurmovies.domain.review;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Review {
    @SerializedName("author_details")
    private Author author;
    private String content;
    private Date updated_at;

    public Review(Author author, String content, Date updated_at) {
        this.author = author;
        this.content = content;
        this.updated_at = updated_at;
    }

    public Author getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }

    public Date getUpdatedAt() {
        return this.updated_at;
    }
}
