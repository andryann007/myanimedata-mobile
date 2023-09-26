package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeResponse (
    @field:SerializedName("data") val animeResults: ArrayList<AnimeResult>? = null
) : Parcelable