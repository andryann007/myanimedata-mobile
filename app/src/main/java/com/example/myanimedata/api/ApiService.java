package com.example.myanimedata.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("anime")
    Call<AnimeResponse> getAllAnime(@Query("page") int page, @Query("limit") int limit,
                                    @Query("order_by") String orderBy, @Query("sort") String sort,
                                    @Query("type") String type);
    @GET("anime")
    Call<AnimeResponse> getPopularAnime(@Query("page") int page, @Query("limit") int limit,
                                        @Query("order_by") String orderBy, @Query("sort") String sort,
                                        @Query("start_date") String startDate, @Query("min_score") int minScore);
    @GET("top/anime")
    Call<AnimeResponse> getTopRatedAnime(@Query("page") int page, @Query("limit") int limit,
                                         @Query("sfw") boolean sfw, @Query("filter") String filter);
    @GET("seasons/now")
    Call<AnimeResponse> getAiringAnime(@Query("page") int page, @Query("limit") int limit,
                                       @Query("sfw") boolean sfw, @Query("unapproved") boolean unapproved);

    @GET("seasons/upcoming")
    Call<AnimeResponse> getUpcomingAnime(@Query("page") int page, @Query("limit") int limit,
                                         @Query("sfw") boolean sfw, @Query("unapproved") boolean unapproved);

    @GET("anime")
    Call<AnimeResponse> searchAllAnime(@Query("page") int page, @Query("limit") int limit,
                                         @Query("q") String q);

    @GET("anime/{id}/full")
    Call<AnimeResponseDetail> getAnimeDetail(@Path("id") int id);

    @GET("manga")
    Call<MangaResponse> getAllManga(@Query("page") int page, @Query("limit") int limit,
                                    @Query("order_by") String orderBy, @Query("sort") String sort);

    @GET("manga")
    Call<MangaResponse> getPopularManga(@Query("page") int page, @Query("limit") int limit,
                                        @Query("order_by") String orderBy, @Query("sort") String sort,
                                        @Query("start_date") String startDate, @Query("status") String status);

    @GET("top/manga")
    Call<MangaResponse> getTopRatedManga(@Query("page") int page, @Query("limit") int limit,
                                         @Query("filter") String filter);

    @GET("manga")
    Call<MangaResponse> searchAllManga(@Query("page") int page, @Query("limit") int limit,
                                    @Query("q") String q);

    @GET("manga/{id}/full")
    Call<MangaResponseDetail> getMangaDetail(@Path("id") int id);

    @GET("characters")
    Call<CharacterResponse> getAllCharacter(@Query("page") int page, @Query("limit") int limit,
                                            @Query("sort") String sort);

    @GET("characters")
    Call<CharacterResponse> getFavoriteCharacter(@Query("page") int page, @Query("limit") int limit,
                                                     @Query("order_by") String order_by, @Query("sort") String sort);

    @GET("top/characters")
    Call<CharacterResponse> getTopCharacter(@Query("page") int page, @Query("limit") int limit);

    @GET("characters")
    Call<CharacterResponse> searchAllCharacter(@Query("page") int page, @Query("limit") int limit,
                                            @Query("q") String q);

    @GET("characters/{id}/full")
    Call<CharacterResponseDetail> getCharacterDetail(@Path("id") int id);
}
