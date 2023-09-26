package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaResult(
    @field:SerializedName("mal_id") val id: Int? = 0,
    @field:SerializedName("title") val title: String? = null,
    @field:SerializedName("images") val imageResults: ImageResult? = null
) : Parcelable