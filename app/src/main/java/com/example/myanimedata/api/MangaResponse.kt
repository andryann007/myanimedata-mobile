package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class MangaResponse {
    @SerializedName("data")
    val mangaResults: List<MangaResult> = ArrayList()
}