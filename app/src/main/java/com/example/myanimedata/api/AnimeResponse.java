package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnimeResponse {
    @SerializedName("data")
    private final List<AnimeResult> animeResults = new ArrayList<>();

    public List<AnimeResult> getAnimeResults() {
        return animeResults;
    }
}
