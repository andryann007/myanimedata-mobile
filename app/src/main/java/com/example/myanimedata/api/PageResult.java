package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class PageResult {
    @SerializedName("current_page")
    private final int currentPage;

    @SerializedName("last_visible_page")
    private final int totalPage;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public PageResult(int currentPage, int totalPage) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }
}
