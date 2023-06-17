package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ImageResult {
    @SerializedName("jpg")
    private final JpgResult jpgResults;

    @SerializedName("webp")
    private final WebpResult webpResults;

    public JpgResult getJpgResults() {
        return jpgResults;
    }

    public WebpResult getWebpResults() {
        return webpResults;
    }

    public ImageResult(JpgResult jpgResults, WebpResult webpResults) {
        this.jpgResults = jpgResults;
        this.webpResults = webpResults;
    }
}
