package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class CharacterResponseDetail {
    @SerializedName("data")
    private final CharacterDetail characterDetail;

    public CharacterDetail getCharacterDetail() {
        return characterDetail;
    }

    public CharacterResponseDetail(CharacterDetail characterDetail) {
        this.characterDetail = characterDetail;
    }
}
