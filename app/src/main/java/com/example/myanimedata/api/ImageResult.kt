package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class ImageResult(
    @field:SerializedName("jpg") val jpgResults: JpgResult, @field:SerializedName(
        "webp"
    ) val webpResults: WebpResult
)