package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoleResult(
    @field:SerializedName("anime") val animeRoleResult: AnimeRoleResult? = null
) : Parcelable