package com.avans.listurmovies.domain.user.retrofit;

import java.util.Date;

public class PostUser {
    private String username;
    private String password;
    private String request_token;

    public PostUser(String username, String password, String request_token) {
        this.username = username;
        this.password = password;
        this.request_token = request_token;
    }

    public String getUsername() {
        return username;
    }

    public String getRequest_token() {
        return request_token;
    }
}
