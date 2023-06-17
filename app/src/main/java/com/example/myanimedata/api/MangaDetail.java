package com.example.myanimedata.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MangaDetail {
    @SerializedName("mal_id")
    private final int id;

    @SerializedName("title")
    private final String title;

    @SerializedName("title_japanese")
    private final String titleJp;

    @SerializedName("status")
    private final String finished;

    @SerializedName("score")
    private final double score;

    @SerializedName("volumes")
    private final int volumes;

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

    public String getTitleJp() {
        return titleJp;
    }

    public String getFinished() {
        return finished;
    }

    public double getScore() {
        return score;
    }

    public int getVolumes() {
        return volumes;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public ImageResult getImageResult() {
        return imageResult;
    }

    public List<GenreResult> getGenreResults() {
        return genreResults;
    }

    public MangaDetail(int id, String title, String titleJp, String finished, double score, int volumes, String synopsis, ImageResult imageResult) {
        this.id = id;
        this.title = title;
        this.titleJp = titleJp;
        this.finished = finished;
        this.score = score;
        this.volumes = volumes;
        this.synopsis = synopsis;
        this.imageResult = imageResult;
    }
}
