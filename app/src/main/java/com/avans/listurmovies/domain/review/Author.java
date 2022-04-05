package com.avans.listurmovies.domain.review;

public class Author {
    private String name;
    private String username;
    private String avatar_path;
    private double rating;

    public Author(String name, String username, String avatar_path, double rating) {
        this.name = name;
        this.username = username;
        this.avatar_path = avatar_path;
        this.rating = rating;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAvatar_path() {
        return this.avatar_path;
    }

    public double getRating() {
        return this.rating;
    }
}
