package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterResponse (
    @field:SerializedName("data") val characterResults: ArrayList<CharacterResult>? = null
) : Parcelable