package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class AnimeDetail(
    @field:SerializedName("mal_id") val id: Int,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("title_japanese") val titleJp: String,
    @field:SerializedName("type") val type: String,
    @field:SerializedName("score") val score: Double,
    @field:SerializedName("episodes") val episodes: Int,
    @field:SerializedName("synopsis") val synopsis: String,
    @field:SerializedName("background") val background: String,
    @field:SerializedName("images") val imageResult: ImageResult
) {

    @SerializedName("genres")
    val genreResults: List<GenreResult> = ArrayList()

}