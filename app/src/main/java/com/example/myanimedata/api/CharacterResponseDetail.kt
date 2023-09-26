package com.example.myanimedata.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterResponseDetail(
    @field:SerializedName("data") val characterDetail: CharacterDetail? = null
) : Parcelable