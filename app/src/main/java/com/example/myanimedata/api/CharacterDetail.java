package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CharacterDetail {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("url")
    private final String url;

    @SerializedName("name")
    private final String name;

    @SerializedName("name_kanji")
    private final String nameKanji;

    @SerializedName("favorites")
    private final int favorites;

    @SerializedName("about")
    private final String about;

    @SerializedName("images")
    private final ImageResult imageResult;

    @SerializedName("anime")
    private final List<RoleResult> roleResults = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getNameKanji() {
        return nameKanji;
    }

    public int getFavorites() {
        return favorites;
    }

    public String getAbout() {
        return about;
    }

    public ImageResult getImageResult() {
        return imageResult;
    }

    public List<RoleResult> getRoleResults() {
        return roleResults;
    }

    public CharacterDetail(int id, String url, String name, String nameKanji, int favorites, String about, ImageResult imageResult) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.nameKanji = nameKanji;
        this.favorites = favorites;
        this.about = about;
        this.imageResult = imageResult;
    }
}
