package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

public class RecommendationResult {
    @SerializedName("entry")
    private final RecommendationResultDetail recommendationResultDetail;

    public RecommendationResultDetail getRecommendationResultDetail() {
        return recommendationResultDetail;
    }

    public RecommendationResult(RecommendationResultDetail recommendationResultDetail) {
        this.recommendationResultDetail = recommendationResultDetail;
    }
}
