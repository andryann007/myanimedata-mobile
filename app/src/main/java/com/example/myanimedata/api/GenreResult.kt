package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreResult (
    @field:SerializedName("mal_id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("type") val type: String,
) : Parcelable