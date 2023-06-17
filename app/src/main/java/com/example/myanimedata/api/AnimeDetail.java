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

    @SerializedName("year")
    private final int year;

    @SerializedName("season")
    private final String season;

    @SerializedName("score")
    private final double score;

    @SerializedName("episodes")
    private final int episodes;

    @SerializedName("synopsis")
    private final String synopsis;

    @SerializedName("images")
    private final ImageResult imageResult;

    @SerializedName("genres")
    private final List<GenreResult> genreResults = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
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

    public String getSeason() {
        return season;
    }

    public ImageResult getImageResult() {
        return imageResult;
    }

    public List<GenreResult> getGenreResults() {
        return genreResults;
    }

    public AnimeDetail(int id, String title, String titleJp, String type, int year, String season, double score, int episodes, String synopsis, ImageResult imageResult) {
        this.id = id;
        this.title = title;
        this.titleJp = titleJp;
        this.type = type;
        this.year = year;
        this.season = season;
        this.score = score;
        this.episodes = episodes;
        this.synopsis = synopsis;
        this.imageResult = imageResult;
    }
}
