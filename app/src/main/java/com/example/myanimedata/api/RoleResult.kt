package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class RoleResult(
    @field:SerializedName("role") val role: String, @field:SerializedName(
        "anime"
    ) val animeRoleResult: AnimeRoleResult
)