package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CharacterResponse {
    @SerializedName("data")
    private final List<CharacterResult> characterResults = new ArrayList<>();

    public List<CharacterResult> getCharacterResults() {
        return characterResults;
    }

    @SerializedName("pagination")
    private CharacterResult paginationCharacter;

    public CharacterResult getPaginationCharacter() {
        return paginationCharacter;
    }
}