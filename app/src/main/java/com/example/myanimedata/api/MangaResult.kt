package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class MangaResult(
    @field:SerializedName("mal_id") val id: Int, @field:SerializedName(
        "title"
    ) val title: String, @field:SerializedName("images") val imageResults: ImageResult
)