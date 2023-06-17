package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class ProducerResult {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("name")
    private final String name;

    @SerializedName("url")
    private final String url;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ProducerResult(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
