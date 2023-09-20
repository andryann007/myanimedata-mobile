package com.example.myanimedata.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("anime")
    fun getAllAnime(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("type") type: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun getPopularAnime(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("order_by") orderBy: String?, @Query("sort") sort: String?,
        @Query("start_date") startDate: String?, @Query("min_score") minScore: Int
    ): Call<AnimeResponse?>?

    @GET("top/anime")
    fun getTopRatedAnime(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("sfw") sfw: Boolean, @Query("filter") filter: String?
    ): Call<AnimeResponse?>?

    @GET("seasons/now")
    fun getAiringAnime(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("sfw") sfw: Boolean, @Query("unapproved") unapproved: Boolean
    ): Call<AnimeResponse?>?

    @GET("seasons/upcoming")
    fun getUpcomingAnime(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("sfw") sfw: Boolean, @Query("unapproved") unapproved: Boolean
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun searchAllAnime(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("q") q: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun filterAnime(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("type") type: String?, @Query("status") status: String?,
        @Query("rating") rating: String?, @Query("order_by") orderBy: String?,
        @Query("sort") sortType: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun filterAnimeType(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("type") type: String?, @Query("order_by") orderBy: String?,
        @Query("sort") sortType: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun filterAnimeStatus(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("status") status: String?, @Query("order_by") orderBy: String?,
        @Query("sort") sortType: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun filterAnimeRating(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("rating") rating: String?, @Query("order_by") orderBy: String?,
        @Query("status") status: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun filterAnimeTypeStatus(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("type") type: String?, @Query("status") status: String?,
        @Query("order_by") orderBy: String?, @Query("sort") sortType: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun filterAnimeTypeRating(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("type") type: String?, @Query("rating") rating: String?,
        @Query("order_by") orderBy: String?, @Query("sort") sortType: String?
    ): Call<AnimeResponse?>?

    @GET("anime")
    fun filterAnimeStatusRating(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("status") status: String?, @Query("rating") rating: String?,
        @Query("order_by") orderBy: String?, @Query("sort") sortType: String?
    ): Call<AnimeResponse?>?

    @GET("anime/{id}/full")
    fun getAnimeDetail(@Path("id") id: Int): Call<AnimeResponseDetail?>?

    @GET("anime/{id}/pictures")
    fun getAnimePictures(@Path("id") id: Int): Call<ImageResponse?>?

    @GET("manga")
    fun getAllManga(@Query("page") page: Int, @Query("limit") limit: Int): Call<MangaResponse?>?

    @GET("manga")
    fun getPopularManga(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("order_by") orderBy: String?, @Query("sort") sort: String?,
        @Query("start_date") startDate: String?, @Query("status") status: String?
    ): Call<MangaResponse?>?

    @GET("top/manga")
    fun getTopRatedManga(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("filter") filter: String?
    ): Call<MangaResponse?>?

    @GET("manga")
    fun searchAllManga(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("q") q: String?
    ): Call<MangaResponse?>?

    @GET("manga/{id}/full")
    fun getMangaDetail(@Path("id") id: Int): Call<MangaResponseDetail?>?

    @GET("manga/{id}/pictures")
    fun getMangaPictures(@Path("id") id: Int): Call<ImageResponse?>?

    @GET("characters")
    fun getAllCharacter(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<CharacterResponse?>?

    @GET("characters")
    fun getFavoriteCharacter(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("order_by") order_by: String?, @Query("sort") sort: String?
    ): Call<CharacterResponse?>?

    @GET("top/characters")
    fun getTopCharacter(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<CharacterResponse?>?

    @GET("characters")
    fun searchAllCharacter(
        @Query("page") page: Int, @Query("limit") limit: Int,
        @Query("q") q: String?
    ): Call<CharacterResponse?>?

    @GET("characters/{id}/full")
    fun getCharacterDetail(@Path("id") id: Int): Call<CharacterResponseDetail?>?
}