package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class TrailerResult {
    @SerializedName("youtube_id")
    private final String youtubeId;

    public String getYoutubeId() {
        return youtubeId;
    }

    public TrailerResult(String youtubeId) {
        this.youtubeId = youtubeId;
    }
}
