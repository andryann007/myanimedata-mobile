package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class ImageResponse(
    @field:SerializedName("data") val imageResultsList: ArrayList<ImageResult>
)