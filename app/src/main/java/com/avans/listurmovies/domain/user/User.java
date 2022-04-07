package com.avans.listurmovies.domain.user;

import java.util.Map;

public class User {
    private int id;
    private String username;
    private Map<String, Object> avatar;
    private boolean include_adult;


    public User(int id, String username, Map<String, Object> avatar, boolean include_adult) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.include_adult = include_adult;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Object> getAvatar() {
        return this.avatar;
    }

    public String getAvatarHash() {
        String userImage = "";
        if(getAvatar() != null) {
            Map<String, Object> gravatar = (Map<String, Object>) getAvatar().get("gravatar");
            userImage = (String) gravatar.get("hash");
        }

        return userImage;
    }

    public boolean getIncludeAdult() {
        return include_adult;
    }
}
