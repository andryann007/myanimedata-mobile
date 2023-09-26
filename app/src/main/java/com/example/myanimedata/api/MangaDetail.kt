package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MangaDetail(
    @field:SerializedName("mal_id") val id: Int? = 0,
    @field:SerializedName("title") val title: String? = null,
    @field:SerializedName("status") val finished: String? = null,
    @field:SerializedName("score") val score: Double? = 0.0,
    @field:SerializedName("synopsis") val synopsis: String? = null,
    @field:SerializedName("background") val background: String? = null,
    @field:SerializedName("images") val imageResult: ImageResult? = null,
    @field:SerializedName("genres") val genreResults: ArrayList<GenreResult>? = null
) : Parcelable