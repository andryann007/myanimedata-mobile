package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class JpgResult {
    @SerializedName("image_url")
    private final String imageUrl;

    @SerializedName("small_image_url")
    private final String smallImageUrl;

    @SerializedName("large_image_url")
    private final String largeImageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public JpgResult(String imageUrl, String smallImageUrl, String largeImageUrl) {
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
        this.largeImageUrl = largeImageUrl;
    }
}
