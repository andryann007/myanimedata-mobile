package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class CharacterResult(
    @field:SerializedName("mal_id") val id: Int,
    @field:SerializedName(
        "name"
    ) val name: String,
    @field:SerializedName("name_kanji") val nameKanji: String,
    @field:SerializedName(
        "about"
    ) val about: String,
    @field:SerializedName("images") val imageResults: ImageResult,
    @field:SerializedName(
        "last_visible_page"
    ) val totalPage: Int,
    @field:SerializedName("current_page") val currentPage: Int,
    @field:SerializedName(
        "count"
    ) val pageCount: Int
)