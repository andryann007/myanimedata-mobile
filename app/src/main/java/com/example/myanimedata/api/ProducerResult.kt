package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProducerResult(
    @field:SerializedName("mal_id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("url") val url: String
) : Parcelable