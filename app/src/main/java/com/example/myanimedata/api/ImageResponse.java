package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageResponse {
    @SerializedName("data")
    private final ArrayList<ImageResult> imageResultsList;

    public ArrayList<ImageResult> getImageResultsList() {
        return imageResultsList;
    }

    public ImageResponse(ArrayList<ImageResult> imageResultsList) {
        this.imageResultsList = imageResultsList;
    }
}
