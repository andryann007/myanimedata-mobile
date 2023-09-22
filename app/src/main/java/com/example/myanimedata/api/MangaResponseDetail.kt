package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class MangaResponseDetail(
    @field:SerializedName("data") val mangaDetails: MangaDetail
)