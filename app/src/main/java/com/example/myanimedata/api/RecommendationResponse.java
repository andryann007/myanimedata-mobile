package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecommendationResponse {
    @SerializedName("data")
    private final ArrayList<RecommendationResult> recommendationResults;

    public ArrayList<RecommendationResult> getRecommendationResults() {
        return recommendationResults;
    }

    public RecommendationResponse(ArrayList<RecommendationResult> recommendationResults) {
        this.recommendationResults = recommendationResults;
    }
}
