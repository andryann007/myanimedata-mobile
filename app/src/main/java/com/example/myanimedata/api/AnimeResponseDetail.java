package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class AnimeResponseDetail {
    @SerializedName("data")
    private final AnimeDetail animeDetails;

    public AnimeDetail getAnimeDetails() {
        return animeDetails;
    }

    public AnimeResponseDetail(AnimeDetail animeDetails) {
        this.animeDetails = animeDetails;
    }
}
