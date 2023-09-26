package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeDetail(
    @field:SerializedName("mal_id") val id: Int? = 0,
    @field:SerializedName("title") val title: String? = null,
    @field:SerializedName("type") val type: String? = null,
    @field:SerializedName("score") val score: Double? = 0.0,
    @field:SerializedName("episodes") val episodes: Int? = null,
    @field:SerializedName("synopsis") val synopsis: String? = null,
    @field:SerializedName("background") val background: String? = null,
    @field:SerializedName("images") val imageResult: ImageResult? = null,
    @field:SerializedName("genres") val genreResults: ArrayList<GenreResult>? = null
) : Parcelable