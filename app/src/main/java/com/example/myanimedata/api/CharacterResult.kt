package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class CharacterResult(
    @field:SerializedName("mal_id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("images") val imageResults: ImageResult
)