package com.example.myanimedata.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import com.example.myanimedata.R;
import com.example.myanimedata.adapter.GenreAdapter;
import com.example.myanimedata.adapter.PictureAdapter;
import com.example.myanimedata.adapter.RecommendationAdapter;
import com.example.myanimedata.adapter.RoleAdapter;
import com.example.myanimedata.api.AnimeResponseDetail;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;
import com.example.myanimedata.api.CharacterResponseDetail;
import com.example.myanimedata.api.GenreResult;
import com.example.myanimedata.api.ImageResponse;
import com.example.myanimedata.api.ImageResult;
import com.example.myanimedata.api.MangaResponseDetail;
import com.example.myanimedata.api.RecommendationResponse;
import com.example.myanimedata.api.RecommendationResult;
import com.example.myanimedata.api.RoleResult;
import com.example.myanimedata.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
    private ApiService apiService;
    private int id;
    private ActivityDetailBinding binding;

    private GenreAdapter genreAdapter;
    private RoleAdapter roleAdapter;
    private PictureAdapter animePictureAdapter, mangaPictureAdapter;
    private RecommendationAdapter animeRecommendationAdapter, mangaRecommendationAdapter;

    private final List<GenreResult> animeGenre = new ArrayList<>();
    private final List<GenreResult> mangaGenre = new ArrayList<>();
    private final List<RoleResult> characterRole = new ArrayList<>();
    private final ArrayList<ImageResult> animePictureResults = new ArrayList<>();
    private final ArrayList<ImageResult> mangaPictureResults = new ArrayList<>();
    private final ArrayList<RecommendationResult> animeRecommendationResults = new ArrayList<>();
    private final ArrayList<RecommendationResult> mangaRecommendationResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        String type = getIntent().getStringExtra("type");

        if(Objects.equals(type, "anime")){
            id = getIntent().getIntExtra("id", 0);
            setAnimeDetails();
        } else if (Objects.equals(type, "manga")){
            id = getIntent().getIntExtra("id", 0);
            setMangaDetails();
        } else if (Objects.equals(type, "character")){
            id = getIntent().getIntExtra("id", 0);
            setCharacterDetails();
        }

        binding.toolbar.setOnClickListener(v-> onBackPressed());
    }

    private void setAnimeDetails() {
        Call<AnimeResponseDetail> call = apiService.getAnimeDetail(id);
        call.enqueue(new Callback<AnimeResponseDetail>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponseDetail> call, @NonNull Response<AnimeResponseDetail> response) {
                if(response.body() != null){
                    binding.loadingDetail.setVisibility(View.GONE);
                    binding.detailScrollView.setVisibility(View.VISIBLE);

                    binding.toolbar.setTitle(HtmlCompat.fromHtml("<b>" +
                                    response.body().getAnimeDetails().getTitle() + "</b>",
                            HtmlCompat.FROM_HTML_MODE_LEGACY));

                    if(response.body().getAnimeDetails().getImageResult().getJpgResults().getLargeImageUrl() != null){
                        binding.imageBackground.setVisibility(View.VISIBLE);

                        Uri backgroundImage = Uri.parse(response.body().getAnimeDetails().getImageResult().getJpgResults().getLargeImageUrl());
                        Picasso.get().load(backgroundImage).into(binding.imageBackground);
                    }

                    if(response.body().getAnimeDetails().getImageResult().getJpgResults().getImageUrl() != null){
                        Uri posterImage = Uri.parse(response.body().getAnimeDetails().getImageResult().getJpgResults().getImageUrl());
                        Picasso.get().load(posterImage).into(binding.imagePoster);
                    } else {
                        binding.imagePoster.setImageResource(R.drawable.ic_no_image);
                        binding.imagePoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }

                    if(response.body().getAnimeDetails().getTitleJp() != null){
                        setTitleJp(binding.textNameAndJapaneseName,
                                response.body().getAnimeDetails().getTitle(),
                                response.body().getAnimeDetails().getTitleJp());
                    } else {
                        setNoTitleJp(binding.textNameAndJapaneseName,
                                response.body().getAnimeDetails().getTitle());
                    }

                    if(response.body().getAnimeDetails().getScore() != 0){
                        setScoreText(binding.textScoreOrPopularity,
                                response.body().getAnimeDetails().getScore());
                    } else {
                        setNoScoreText(binding.textScoreOrPopularity);
                    }

                    if(response.body().getAnimeDetails().getType().equalsIgnoreCase("tv")){
                        if(response.body().getAnimeDetails().getEpisodes() != 0){
                            setTvEpisodesText(binding.textTypeAndEpisodes,
                                    response.body().getAnimeDetails().getType(),
                                    response.body().getAnimeDetails().getEpisodes());
                        } else {
                            setTypeText(binding.textTypeAndEpisodes, response.body().getAnimeDetails().getType());
                        }
                    } else {
                        setTypeText(binding.textTypeAndEpisodes, response.body().getAnimeDetails().getType());
                    }

                    if(response.body().getAnimeDetails().getSynopsis() != null){
                        binding.titleSynopsisOrAbout.setVisibility(View.VISIBLE);

                        binding.textSynopsisOrAbout.setText(response.body().getAnimeDetails().getSynopsis());
                        binding.textSynopsisOrAbout.setVisibility(View.VISIBLE);
                    }

                    if(response.body().getAnimeDetails().getBackground() != null){
                        binding.titleBackground.setVisibility(View.VISIBLE);

                        binding.textBackground.setText(response.body().getAnimeDetails().getBackground());
                        binding.textBackground.setVisibility(View.VISIBLE);
                    }

                    setGenresAnime();

                    setPicturesAnime();
                } else {
                    binding.loadingDetail.setVisibility(View.GONE);
                    binding.textNoDetailResult.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(@NonNull Call<AnimeResponseDetail> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this,"Fail to Fetch Data !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setGenresAnime(){
        genreAdapter = new GenreAdapter(animeGenre, this);
        binding.rvGenreOrDebutList.setAdapter(genreAdapter);

        getGenresAnime();
    }

    private void getGenresAnime() {
        Call<AnimeResponseDetail> call = apiService.getAnimeDetail(id);
        call.enqueue(new Callback<AnimeResponseDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AnimeResponseDetail> call, @NonNull Response<AnimeResponseDetail> response) {
                assert response.body() != null;
                if(response.body().getAnimeDetails().getGenreResults() !=null){
                    binding.textGenreOrDebutList.setVisibility(View.VISIBLE);
                    binding.rvGenreOrDebutList.setVisibility(View.VISIBLE);

                    int oldCount = animeGenre.size();
                    animeGenre.addAll(response.body().getAnimeDetails().getGenreResults());
                    genreAdapter.notifyItemChanged(oldCount, animeGenre.size());
                } else {
                    setNoText(binding.textGenreOrDebutList, "No Genre Yet !!!");

                    binding.textGenreOrDebutList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponseDetail> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Genre !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPicturesAnime(){
        animePictureAdapter = new PictureAdapter(animePictureResults);
        binding.rvPictureList.setAdapter(animePictureAdapter);

        getPicturesAnime();
    }

    private void getPicturesAnime(){
        Call<ImageResponse> call = apiService.getAnimePictures(id);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<ImageResponse> call, @NonNull Response<ImageResponse> response) {
                assert response.body() != null;
                if(response.body().getImageResultsList() != null){
                    binding.textPictureList.setVisibility(View.VISIBLE);
                    binding.rvPictureList.setVisibility(View.VISIBLE);

                    int oldCount = animePictureResults.size();
                    animePictureResults.addAll(response.body().getImageResultsList());
                    animePictureAdapter.notifyItemChanged(oldCount, animePictureResults.size());
                } else {
                    setNoText(binding.textPictureList, "No Images Result !!!");

                    binding.textPictureList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageResponse> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Pictures !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecommendationAnime(){
        animeRecommendationAdapter = new RecommendationAdapter(animeRecommendationResults);
        binding.rvRecommendationList.setAdapter(animeRecommendationAdapter);

        getRecommendationAnime();
    }

    private void getRecommendationAnime(){
        Call<RecommendationResponse> call = apiService.getAnimeRecommendations(id);
        call.enqueue(new Callback<RecommendationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecommendationResponse> call, @NonNull Response<RecommendationResponse> response) {
                assert response.body() != null;
                if(response.body().getRecommendationResults() != null){
                    binding.textRecommendationList.setVisibility(View.VISIBLE);
                    binding.rvRecommendationList.setVisibility(View.VISIBLE);

                    int oldCount = animeRecommendationResults.size();
                    animeRecommendationResults.addAll(response.body().getRecommendationResults());
                    animeRecommendationAdapter.notifyItemChanged(oldCount, animeRecommendationResults.size());
                } else {
                    setNoText(binding.textRecommendationList, "No Recommendation Result !!!");

                    binding.textRecommendationList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendationResponse> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this,"Fail to Fetch Anime Recommendations !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMangaDetails() {
        Call<MangaResponseDetail> call = apiService.getMangaDetail(id);
        call.enqueue(new Callback<MangaResponseDetail>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponseDetail> call, @NonNull Response<MangaResponseDetail> response) {
                if(response.body() != null){
                    binding.loadingDetail.setVisibility(View.GONE);
                    binding.detailScrollView.setVisibility(View.VISIBLE);

                    binding.toolbar.setTitle(HtmlCompat.fromHtml("<b>" +
                            response.body().getMangaDetails().getTitle() + "</b>",
                            HtmlCompat.FROM_HTML_MODE_LEGACY));

                    if(response.body().getMangaDetails().getImageResult().getJpgResults().getLargeImageUrl() != null){
                        binding.imageBackground.setVisibility(View.VISIBLE);

                        Uri backgroundImage = Uri.parse(response.body().getMangaDetails().getImageResult().getJpgResults().getLargeImageUrl());
                        Picasso.get().load(backgroundImage).into(binding.imageBackground);
                    }

                    if(response.body().getMangaDetails().getImageResult().getJpgResults().getImageUrl() != null){
                        Uri posterImage = Uri.parse(response.body().getMangaDetails().getImageResult().getJpgResults().getImageUrl());
                        Picasso.get().load(posterImage).into(binding.imagePoster);
                    } else {
                        binding.imagePoster.setImageResource(R.drawable.ic_no_image);
                        binding.imagePoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }

                    if(response.body().getMangaDetails().getTitleJp() != null){
                        setTitleJp(binding.textNameAndJapaneseName,
                                response.body().getMangaDetails().getTitle(),
                                response.body().getMangaDetails().getTitleJp());
                    } else {
                        setNoTitleJp(binding.textNameAndJapaneseName,
                                response.body().getMangaDetails().getTitle());
                    }

                    if(response.body().getMangaDetails().getScore() != 0){
                        setScoreText(binding.textScoreOrPopularity,
                                response.body().getMangaDetails().getScore());
                    } else {
                        setNoScoreText(binding.textScoreOrPopularity);
                    }

                    if(response.body().getMangaDetails().getFinished() != null){
                        setStatusText(binding.textTypeAndEpisodes, response.body().getMangaDetails().getFinished());
                    } else {
                        setStatusText(binding.textTypeAndEpisodes, "-");
                    }

                    if(response.body().getMangaDetails().getSynopsis() != null){
                        binding.titleSynopsisOrAbout.setVisibility(View.VISIBLE);

                        binding.textSynopsisOrAbout.setText(response.body().getMangaDetails().getSynopsis());
                        binding.textSynopsisOrAbout.setVisibility(View.VISIBLE);
                    }

                    if(response.body().getMangaDetails().getBackground() != null){
                        binding.titleBackground.setVisibility(View.VISIBLE);

                        binding.textBackground.setText(response.body().getMangaDetails().getBackground());
                        binding.textBackground.setVisibility(View.VISIBLE);
                    }

                    setGenresMangas();

                    setPicturesMangas();
                } else {
                    binding.loadingDetail.setVisibility(View.GONE);
                    binding.textNoDetailResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponseDetail> call, @NonNull Throwable t) {
                binding.loadingDetail.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this,"Fail to Fetch Data !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setGenresMangas(){
        genreAdapter = new GenreAdapter(mangaGenre, this);
        binding.rvGenreOrDebutList.setAdapter(genreAdapter);

        getGenresMangas();
    }

    private void getGenresMangas() {
        Call<MangaResponseDetail> call = apiService.getMangaDetail(id);
        call.enqueue(new Callback<MangaResponseDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MangaResponseDetail> call, @NonNull Response<MangaResponseDetail> response) {
                assert response.body() != null;
                if(response.body().getMangaDetails().getGenreResults() != null){
                    binding.textGenreOrDebutList.setVisibility(View.VISIBLE);
                    binding.rvGenreOrDebutList.setVisibility(View.VISIBLE);

                    int oldCount = mangaGenre.size();
                    mangaGenre.addAll(response.body().getMangaDetails().getGenreResults());
                    genreAdapter.notifyItemChanged(oldCount, mangaGenre.size());
                } else {
                    binding.textGenreOrDebutList.setText("No Genre Yet !!!");

                    binding.textGenreOrDebutList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponseDetail> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Genre !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPicturesMangas(){
        mangaPictureAdapter = new PictureAdapter(mangaPictureResults);
        binding.rvPictureList.setAdapter(mangaPictureAdapter);

        getPicturesMangas();
    }

    private void getPicturesMangas(){
        Call<ImageResponse> call = apiService.getMangaPictures(id);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<ImageResponse> call, @NonNull Response<ImageResponse> response) {
                assert response.body() != null;
                if(response.body().getImageResultsList() != null){
                    binding.textPictureList.setVisibility(View.VISIBLE);
                    binding.rvPictureList.setVisibility(View.VISIBLE);

                    int oldCount = mangaPictureResults.size();
                    mangaPictureResults.addAll(response.body().getImageResultsList());
                    mangaPictureAdapter.notifyItemChanged(oldCount, mangaPictureResults.size());
                } else {
                    setNoText(binding.textPictureList, "No Images Result !!!");

                    binding.textPictureList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageResponse> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Pictures !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecommendationMangas(){
        mangaRecommendationAdapter = new RecommendationAdapter(mangaRecommendationResults);
        binding.rvRecommendationList.setAdapter(mangaRecommendationAdapter);

        getRecommendationMangas();
    }

    private void getRecommendationMangas(){
        Call<RecommendationResponse> call = apiService.getMangaRecommendations(id);
        call.enqueue(new Callback<RecommendationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecommendationResponse> call, @NonNull Response<RecommendationResponse> response) {
                assert response.body() != null;
                if(response.body().getRecommendationResults() != null){
                    binding.textRecommendationList.setVisibility(View.VISIBLE);
                    binding.rvRecommendationList.setVisibility(View.VISIBLE);

                    int oldCount = mangaRecommendationResults.size();
                    mangaRecommendationResults.addAll(response.body().getRecommendationResults());
                    mangaRecommendationAdapter.notifyItemChanged(oldCount, mangaRecommendationResults.size());
                } else {
                    setNoText(binding.textRecommendationList, "No Recommendation Result !!!");

                    binding.textRecommendationList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendationResponse> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this,"Fail to Fetch Anime Recommendations !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCharacterDetails() {
        Call<CharacterResponseDetail> call = apiService.getCharacterDetail(id);
        call.enqueue(new Callback<CharacterResponseDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CharacterResponseDetail> call, @NonNull Response<CharacterResponseDetail> response) {
                if(response.body() != null){
                    binding.loadingDetail.setVisibility(View.GONE);
                    binding.detailScrollView.setVisibility(View.VISIBLE);

                    binding.toolbar.setTitle(HtmlCompat.fromHtml("<b>" +
                                    response.body().getCharacterDetail().getName() + "</b>",
                            HtmlCompat.FROM_HTML_MODE_LEGACY));

                    if(response.body().getCharacterDetail().getImageResult().getJpgResults().getImageUrl() != null){
                        Uri posterImage = Uri.parse(response.body().getCharacterDetail().getImageResult().getJpgResults().getImageUrl());
                        Picasso.get().load(posterImage).into(binding.imagePoster);
                    } else {
                        binding.imagePoster.setImageResource(R.drawable.ic_no_image);
                        binding.imagePoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }

                    if(response.body().getCharacterDetail().getNameKanji() != null){
                        setTitleJp(binding.textNameAndJapaneseName,
                                response.body().getCharacterDetail().getName(),
                                response.body().getCharacterDetail().getNameKanji());
                    } else {
                        setNoTitleJp(binding.textNameAndJapaneseName,
                                response.body().getCharacterDetail().getName());
                    }

                    if(response.body().getCharacterDetail().getFavorites() != 0){
                        setCharacterScoreText(binding.textScoreOrPopularity,
                                response.body().getCharacterDetail().getFavorites());
                    } else {
                        setNoText(binding.textScoreOrPopularity, "No Favorites Yet !!!");
                    }

                    if(response.body().getCharacterDetail().getUrl() != null){
                        setCharacterUrlText(binding.textTypeAndEpisodes, response.body().getCharacterDetail().getUrl());
                    } else {
                        setCharacterUrlText(binding.textTypeAndEpisodes, "-");
                    }

                    if(response.body().getCharacterDetail().getAbout() != null){
                        binding.titleSynopsisOrAbout.setText("About :");
                        binding.titleSynopsisOrAbout.setVisibility(View.VISIBLE);

                        binding.textSynopsisOrAbout.setText(response.body().getCharacterDetail().getAbout());
                        binding.textSynopsisOrAbout.setVisibility(View.VISIBLE);
                    }

                    setCharacterRole();
                } else {
                    binding.loadingDetail.setVisibility(View.GONE);
                    binding.textNoDetailResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponseDetail> call, @NonNull Throwable t) {
                binding.loadingDetail.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this,"Fail to Fetch Data !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCharacterRole(){
        roleAdapter = new RoleAdapter(characterRole, this);
        binding.rvGenreOrDebutList.setAdapter(roleAdapter);

        getCharacterRole();
    }

    private void getCharacterRole() {
        Call<CharacterResponseDetail> call = apiService.getCharacterDetail(id);
        call.enqueue(new Callback<CharacterResponseDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CharacterResponseDetail> call, @NonNull Response<CharacterResponseDetail> response) {
                assert response.body() != null;
                if(response.body().getCharacterDetail().getRoleResults() != null){
                    binding.textGenreOrDebutList.setVisibility(View.VISIBLE);
                    binding.rvGenreOrDebutList.setVisibility(View.VISIBLE);

                    int oldCount = characterRole.size();
                    characterRole.addAll(response.body().getCharacterDetail().getRoleResults());
                    roleAdapter.notifyItemChanged(oldCount, characterRole.size());
                } else {
                    binding.textGenreOrDebutList.setText("No Debut Yet !!!");

                    binding.textGenreOrDebutList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponseDetail> call, @NonNull Throwable t) {
                binding.loadingDetail.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Role !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTitleJp(TextView tv, String originalName, String jpName){
        tv.setText(HtmlCompat.fromHtml("<b>" + originalName + "<br>(" + jpName + ")</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setNoTitleJp(TextView tv, String originalName){
        tv.setText(HtmlCompat.fromHtml("<b>" + originalName + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setScoreText(TextView tv, double score){
        tv.setText(HtmlCompat.fromHtml("<b>Score : " + score + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setNoScoreText(TextView tv){
        tv.setText(HtmlCompat.fromHtml("<font color='#FF2400'><b> No Score Yet !!!</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setTvEpisodesText(TextView tv, String type, int episodes){
        tv.setText(HtmlCompat.fromHtml("<b>" + type + " (" + episodes + " Episodes)</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setTypeText(TextView tv, String type){
        tv.setText(HtmlCompat.fromHtml("<b>" + type + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setNoText(TextView tv, String note){
        tv.setText(HtmlCompat.fromHtml("<font color='#FF2400'><b>" + note + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setStatusText(TextView tv, String status){
        tv.setText(HtmlCompat.fromHtml("<b>Status : " + status + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterScoreText(TextView tv, int favorites){
        tv.setText(HtmlCompat.fromHtml("<b>Favorites : " + favorites + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterUrlText(TextView tv, String url){
        tv.setText(HtmlCompat.fromHtml("<b>URL : " + url + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}