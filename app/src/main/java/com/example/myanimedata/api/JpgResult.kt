package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class JpgResult(
    @field:SerializedName("image_url") val imageUrl: String, @field:SerializedName(
        "small_image_url"
    ) val smallImageUrl: String, @field:SerializedName("large_image_url") val largeImageUrl: String
)