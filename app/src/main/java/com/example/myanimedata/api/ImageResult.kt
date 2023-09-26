package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageResult(
    @field:SerializedName("jpg") val jpgResults: JpgResult? = null,
    @field:SerializedName("webp") val webpResults: WebpResult? = null
) : Parcelable