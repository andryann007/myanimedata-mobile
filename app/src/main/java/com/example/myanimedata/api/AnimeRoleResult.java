package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class AnimeRoleResult {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("url")
    private final String url;

    @SerializedName("title")
    private final String title;

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public AnimeRoleResult(int id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }
}
