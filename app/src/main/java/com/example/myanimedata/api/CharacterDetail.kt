package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CharacterDetail(
    @field:SerializedName("mal_id") val id: Int? = 0,
    @field:SerializedName("url") val url: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("favorites") val favorites: Int? = 0,
    @field:SerializedName("about") val about: String? = null,
    @field:SerializedName("images") val imageResult: ImageResult? = null,
    @field:SerializedName("anime") val roleResults: ArrayList<RoleResult>? = null
) : Parcelable