package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class AnimeRoleResult(
    @field:SerializedName("mal_id") val id: Int, @field:SerializedName(
        "url"
    ) val url: String, @field:SerializedName("title") val title: String
)