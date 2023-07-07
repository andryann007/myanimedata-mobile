package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnimeDetail {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("title")
    private final String title;

    @SerializedName("title_japanese")
    private final String titleJp;

    @SerializedName("type")
    private final String type;

    @SerializedName("score")
    private final double score;

    @SerializedName("episodes")
    private final int episodes;

    @SerializedName("synopsis")
    private final String synopsis;

    @SerializedName("background")
    private final String background;

    @SerializedName("images")
    private final ImageResult imageResult;

    @SerializedName("trailer")
    private final TrailerResult trailerResult;

    @SerializedName("genres")
    private final List<GenreResult> genreResults = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public int getEpisodes() {
        return episodes;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getTitleJp() {
        return titleJp;
    }

    public String getType() {
        return type;
    }

    public ImageResult getImageResult() {
        return imageResult;
    }

    public TrailerResult getTrailerResult() {
        return trailerResult;
    }

    public String getBackground() {
        return background;
    }

    public List<GenreResult> getGenreResults() {
        return genreResults;
    }

    public AnimeDetail(int id, String title, String titleJp, String type, double score, int episodes,
                       String synopsis, String background, ImageResult imageResult,
                       TrailerResult trailerResult) {
        this.id = id;
        this.title = title;
        this.titleJp = titleJp;
        this.type = type;
        this.score = score;
        this.episodes = episodes;
        this.synopsis = synopsis;
        this.background = background;
        this.imageResult = imageResult;
        this.trailerResult = trailerResult;
    }
}
