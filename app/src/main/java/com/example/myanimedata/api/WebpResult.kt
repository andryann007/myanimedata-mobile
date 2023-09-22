package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebpResult(
    @field:SerializedName("image_url") val imageUrl: String,
    @field:SerializedName("large_image_url") val largeImageUrl: String
) : Parcelable