package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaResponse (
    @field:SerializedName("data") val mangaResults: ArrayList<MangaResult>? = null
) : Parcelable