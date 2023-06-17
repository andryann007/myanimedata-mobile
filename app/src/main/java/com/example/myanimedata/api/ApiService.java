package com.example.myanimedata.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("anime")
    Call<AnimeResponse> getAllAnime(@Query("page") int page, @Query("limit") int limit);
    @GET("anime")
    Call<AnimeResponse> getTopRatedAnime(@Query("page") int page, @Query("limit") int limit,
                                         @Query("order_by") String order_by);

    @GET("anime")
    Call<AnimeResponse> getPopularAnime(@Query("page") int page, @Query("limit") int limit,
                                        @Query("order_by") String order_by);
    @GET("anime")
    Call<AnimeResponse> getAiringAnime(@Query("page") int page, @Query("limit") int limit,
                                       @Query("status") String status, @Query("order_by") String order_by);

    @GET("anime")
    Call<AnimeResponse> getUpcomingAnime(@Query("page") int page, @Query("limit") int limit,
                                         @Query("status") String status);

    @GET("anime/{id}/full")
    Call<AnimeResponseDetail> getAnimeDetail(@Path("id") int id);

    @GET("manga")
    Call<MangaResponse> getAllManga(@Query("page") int page, @Query("limit") int limit);

    @GET("manga")
    Call<MangaResponse> getPopularManga(@Query("page") int page, @Query("limit") int limit,
                                        @Query("order_by") String order_by);

    @GET("manga")
    Call<MangaResponse> getTopRatedManga(@Query("page") int page, @Query("limit") int limit,
                                         @Query("status") String status, @Query("order_by") String order_by);

    @GET("manga/{id}/full")
    Call<MangaResponseDetail> getMangaDetail(@Path("id") int id);

    @GET("characters")
    Call<CharacterResponse> getAllCharacter(@Query("page") int page, @Query("limit") int limit);

    @GET("characters")
    Call<CharacterResponse> getFavoriteCharacterList(@Query("order_by") String order_by, @Query("page") int page,
                                                     @Query("limit") int limit);

    @GET("characters/{id}/full")
    Call<CharacterResponseDetail> getCharacterDetail(@Path("id") int id);
}
