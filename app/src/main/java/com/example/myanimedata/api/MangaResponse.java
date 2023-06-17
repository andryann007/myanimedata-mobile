package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MangaResponse {
    @SerializedName("data")
    private final List<MangaResult> mangaResults = new ArrayList<>();

    public List<MangaResult> getMangaResults() {
        return mangaResults;
    }
}
