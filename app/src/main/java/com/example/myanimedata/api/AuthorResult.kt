package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorResult(
    @field:SerializedName("mal_id") val id: Int? = 0,
    @field:SerializedName("name") val name: String? = null
) : Parcelable