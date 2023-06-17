package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class TrailerResult {
    @SerializedName("youtube_id")
    private final String youtubeId;

    @SerializedName("url")
    private final String url;

    @SerializedName("embed_url")
    private final String embedUrl;

    public String getYoutubeId() {
        return youtubeId;
    }

    public String getUrl() {
        return url;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public TrailerResult(String youtubeId, String url, String embedUrl) {
        this.youtubeId = youtubeId;
        this.url = url;
        this.embedUrl = embedUrl;
    }
}
