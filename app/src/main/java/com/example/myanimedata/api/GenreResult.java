package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class GenreResult {
    @SerializedName("mal_id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
