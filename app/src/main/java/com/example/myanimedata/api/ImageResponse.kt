package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageResponse(
    @field:SerializedName("data") val imageResultsList: ArrayList<ImageResult>? = null
) : Parcelable