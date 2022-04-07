package com.avans.listurmovies.domain.movie;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("key")
    private String videoUrl;
    private String site;
    private String type;

    public Video(String videoUrl, String site, String type) {
        this.videoUrl = videoUrl;
        this.site = site;
        this.type = type;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }
}
