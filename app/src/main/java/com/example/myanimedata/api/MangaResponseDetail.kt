package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaResponseDetail(
    @field:SerializedName("data") val mangaDetails: MangaDetail? = null
) : Parcelable