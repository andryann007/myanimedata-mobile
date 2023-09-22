package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class MangaDetail(
    @field:SerializedName("mal_id") val id: Int,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("status") val finished: String,
    @field:SerializedName("score") val score: Double,
    @field:SerializedName("synopsis") val synopsis: String,
    @field:SerializedName("background") val background: String,
    @field:SerializedName("images") val imageResult: ImageResult
) {

    @SerializedName("genres")
    val genreResults: List<GenreResult> = ArrayList()

}