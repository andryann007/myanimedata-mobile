package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeResponseDetail(
    @field:SerializedName("data") val animeDetails: AnimeDetail? = null
) : Parcelable