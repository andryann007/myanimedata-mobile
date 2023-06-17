package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class MangaResponseDetail {
    @SerializedName("data")
    private final MangaDetail mangaDetails;

    public MangaDetail getMangaDetails() {
        return mangaDetails;
    }

    public MangaResponseDetail(MangaDetail mangaDetails) {
        this.mangaDetails = mangaDetails;
    }
}
