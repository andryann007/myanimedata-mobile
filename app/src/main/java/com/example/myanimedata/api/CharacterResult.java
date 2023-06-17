package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class CharacterResult {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("name")
    private final String name;

    @SerializedName("name_kanji")
    private final String nameKanji;

    @SerializedName("about")
    private final String about;

    @SerializedName("images")
    private final ImageResult imageResults;

    @SerializedName("last_visible_page")
    private final int totalPage;

    @SerializedName("current_page")
    private final int currentPage;

    @SerializedName("count")
    private final int pageCount;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameKanji() {
        return nameKanji;
    }

    public String getAbout() {
        return about;
    }

    public ImageResult getImageResults() {
        return imageResults;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public CharacterResult(int id, String name, String nameKanji, String about, ImageResult imageResults, int totalPage, int currentPage, int pageCount) {
        this.id = id;
        this.name = name;
        this.nameKanji = nameKanji;
        this.about = about;
        this.imageResults = imageResults;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.pageCount = pageCount;
    }
}
