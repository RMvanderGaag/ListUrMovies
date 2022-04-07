package com.avans.listurmovies.domain.user;

public class UserRequestToken {
    private String request_token;
    private String session_id;

    public UserRequestToken(String request_token) {
        this.request_token = request_token;
    }

    public String getRequest_token() {
        return this.request_token;
    }

    public String getSession_id() {
        return session_id;
    }
}
