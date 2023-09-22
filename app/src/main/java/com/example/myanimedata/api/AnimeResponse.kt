package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class AnimeResponse {
    @SerializedName("data") val animeResults: List<AnimeResult> = ArrayList()
}