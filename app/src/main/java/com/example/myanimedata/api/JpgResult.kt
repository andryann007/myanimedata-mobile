package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JpgResult(
    @field:SerializedName("image_url") val imageUrl: String? = null,
    @field:SerializedName("large_image_url") val largeImageUrl: String? = null
) : Parcelable