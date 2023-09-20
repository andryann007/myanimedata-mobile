package com.example.myanimedata.api

import com.google.gson.annotations.SerializedName

class PageResult(
    @field:SerializedName("current_page") val currentPage: Int, @field:SerializedName(
        "last_visible_page"
    ) val totalPage: Int
)