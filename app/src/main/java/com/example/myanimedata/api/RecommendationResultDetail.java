package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class RecommendationResultDetail {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("images")
    private final ImageResult imageResult;

    @SerializedName("title")
    private final String title;

    public final int getId() {
        return id;
    }

    public ImageResult getImageResult() {
        return imageResult;
    }

    public String getTitle() {
        return title;
    }

    public RecommendationResultDetail(int id, ImageResult imageResult, String title) {
        this.id = id;
        this.imageResult = imageResult;
        this.title = title;
    }
}
