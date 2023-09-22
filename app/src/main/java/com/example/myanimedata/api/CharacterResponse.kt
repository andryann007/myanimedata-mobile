package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class CharacterResponse {
    @SerializedName("data")
    val characterResults: List<CharacterResult> = ArrayList()
}