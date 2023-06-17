package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class MangaResult {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("title")
    private final String title;

    @SerializedName("images")
    private final ImageResult imageResults;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ImageResult getImageResults() {
        return imageResults;
    }

    public MangaResult(int id, String title, ImageResult imageResults) {
        this.id = id;
        this.title = title;
        this.imageResults = imageResults;
    }
}
