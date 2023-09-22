package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class CharacterDetail(
    @field:SerializedName("mal_id") val id: Int,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("favorites") val favorites: Int,
    @field:SerializedName("about") val about: String,
    @field:SerializedName("images") val imageResult: ImageResult
) {
    @SerializedName("anime")
    val roleResults: List<RoleResult> = ArrayList()
}