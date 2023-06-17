package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class RoleResult {
    @SerializedName("role")
    private final String role;

    @SerializedName("anime")
    private final AnimeRoleResult animeRoleResult;

    public String getRole() {
        return role;
    }

    public AnimeRoleResult getAnimeRoleResult() {
        return animeRoleResult;
    }

    public RoleResult(String role, AnimeRoleResult animeRoleResult) {
        this.role = role;
        this.animeRoleResult = animeRoleResult;
    }
}
