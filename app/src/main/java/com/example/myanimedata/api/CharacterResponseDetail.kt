package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class CharacterResponseDetail(
    @field:SerializedName("data") val characterDetail: CharacterDetail
)