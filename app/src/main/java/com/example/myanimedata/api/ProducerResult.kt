package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class ProducerResult(
    @field:SerializedName("mal_id") val id: Int, @field:SerializedName(
        "name"
    ) val name: String, @field:SerializedName("url") val url: String
)