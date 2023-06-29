package com.example.myanimedata.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import com.example.myanimedata.R;
import com.example.myanimedata.adapter.GenreAdapter;
import com.example.myanimedata.adapter.RoleAdapter;
import com.example.myanimedata.api.AnimeResponseDetail;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;
import com.example.myanimedata.api.CharacterResponseDetail;
import com.example.myanimedata.api.GenreResult;
import com.example.myanimedata.api.MangaResponseDetail;
import com.example.myanimedata.api.RoleResult;
import com.example.myanimedata.databinding.ActivityDetailBinding;
import com.makeramen.roundedimageview.RoundedImageView;
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

    private final List<GenreResult> animeGenre = new ArrayList<>();
    private final List<GenreResult> mangaGenre = new ArrayList<>();
    private final List<RoleResult> characterRole = new ArrayList<>();

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

                    RoundedImageView roundedImageView = findViewById(R.id.imagePoster);
                    String imageUrl = response.body().getAnimeDetails().getImageResult().getJpgResults().getImageUrl();
                    Uri mImageUrl = Uri.parse(imageUrl);
                    Picasso.get().load(mImageUrl).into(roundedImageView);

                    String mTitle = response.body().getAnimeDetails().getTitle();
                    setHtmlText(binding.textTitleOrName, mTitle);

                    String mAliasTitle = response.body().getAnimeDetails().getTitleJp();
                    setHtmlText(binding.textTitleOrNameAlias, mAliasTitle);

                    String mYear = String.valueOf(response.body().getAnimeDetails().getYear());
                    String mSeason = response.body().getAnimeDetails().getSeason();
                    if(!mYear.equalsIgnoreCase("0")){
                        if(mSeason != null){
                            setYearText(binding.textReleaseYear, mSeason, mYear);
                        } else {
                            setYearText(binding.textReleaseYear, "", mYear);
                        }
                    } else {
                        if(mSeason != null){
                            setYearText(binding.textReleaseYear, mSeason, "");
                        } else {
                            setYearText(binding.textReleaseYear, "", "");
                        }
                    }

                    String mScore = String.valueOf(response.body().getAnimeDetails().getScore());
                    if(!mScore.equalsIgnoreCase("0")){
                        setScoresText(binding.textScoreOrRating, mScore);
                    } else {
                        setScoresText(binding.textScoreOrRating, "No Score Yet !!!");
                    }

                    String mType = response.body().getAnimeDetails().getType();
                    String mEpisodes = String.valueOf(response.body().getAnimeDetails().getEpisodes());
                    if(mType != null){
                        if (!mEpisodes.equalsIgnoreCase("0")){
                            setEpisodesText(binding.textEpisodeOrVolume, mType, mEpisodes);
                        } else {
                            setEpisodesText(binding.textEpisodeOrVolume, mType, "-");
                        }
                    }

                    String mSynopsis = response.body().getAnimeDetails().getSynopsis();
                    if(mSynopsis != null){
                        setHtmlText(binding.textSynopsis, mSynopsis);
                    } else {
                        setHtmlText(binding.textSynopsis, "No Synopsis Yet !!!");
                    }

                    setGenresAnime();
                } else {
                    binding.loadingDetail.setVisibility(View.GONE);
                    binding.textNoDetailResult.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(@NonNull Call<AnimeResponseDetail> call, @NonNull Throwable t) {
                binding.loadingDetail.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this,"Fail to Fetch Data !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setGenresAnime(){
        genreAdapter = new GenreAdapter(animeGenre, this);
        binding.rvGenreList.setAdapter(genreAdapter);

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
                    int oldCount = animeGenre.size();
                    animeGenre.addAll(response.body().getAnimeDetails().getGenreResults());
                    genreAdapter.notifyItemChanged(oldCount, animeGenre.size());
                } else {
                    binding.textGenreList.setText("No Genre Yet !!!");
                    binding.rvGenreList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponseDetail> call, @NonNull Throwable t) {
                binding.loadingDetail.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Genre !!!", Toast.LENGTH_SHORT).show();
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

                    RoundedImageView roundedImageView = findViewById(R.id.imagePoster);
                    String imageUrl = response.body().getMangaDetails().getImageResult().getJpgResults().getImageUrl();
                    Uri mImageUrl = Uri.parse(imageUrl);
                    Picasso.get().load(mImageUrl).into(roundedImageView);

                    String mTitle = response.body().getMangaDetails().getTitle();
                    setHtmlText(binding.textTitleOrName, mTitle);

                    String mAliasTitle = response.body().getMangaDetails().getTitleJp();
                    setHtmlText(binding.textTitleOrNameAlias, mAliasTitle);

                    String mStatus = response.body().getMangaDetails().getFinished();
                    if(mStatus != null){
                        setStatusText(binding.textReleaseYear, mStatus);
                    } else {
                        setStatusText(binding.textReleaseYear, "-");
                    }

                    String mScore = String.valueOf(response.body().getMangaDetails().getScore());
                    if(!mScore.equalsIgnoreCase("0")){
                        setScoresText(binding.textScoreOrRating, mScore);
                    } else {
                        setScoresText(binding.textScoreOrRating, "-");
                    }

                    String mVolume = String.valueOf(response.body().getMangaDetails().getVolumes());
                    if(!mVolume.equalsIgnoreCase("0")){
                        setVolumeText(binding.textEpisodeOrVolume, mVolume);
                    } else {
                        setVolumeText(binding.textEpisodeOrVolume, "-");
                    }

                    String mSynopsis = response.body().getMangaDetails().getSynopsis();
                    if(mSynopsis != null){
                        setHtmlText(binding.textSynopsis, mSynopsis);
                    } else {
                        setHtmlText(binding.textSynopsis, "No Synopsis Yet !!!");
                    }

                    setGenresMangas();
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
        binding.rvGenreList.setAdapter(genreAdapter);

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
                    int oldCount = mangaGenre.size();
                    mangaGenre.addAll(response.body().getMangaDetails().getGenreResults());
                    genreAdapter.notifyItemChanged(oldCount, mangaGenre.size());
                } else {
                    binding.textGenreList.setText("No Genre Yet !!!");
                    binding.rvGenreList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponseDetail> call, @NonNull Throwable t) {
                binding.loadingDetail.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Genre !!!", Toast.LENGTH_SHORT).show();
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

                    RoundedImageView roundedImageView = findViewById(R.id.imagePoster);
                    String imageUrl = response.body().getCharacterDetail().getImageResult().getJpgResults().getImageUrl();
                    Uri mImageUrl = Uri.parse(imageUrl);
                    Picasso.get().load(mImageUrl).into(roundedImageView);

                    String mTitle = response.body().getCharacterDetail().getName();
                    setHtmlText(binding.textTitleOrName, mTitle);

                    String mAliasTitle = response.body().getCharacterDetail().getNameKanji();
                    setHtmlText(binding.textTitleOrNameAlias, mAliasTitle);

                    String mName = response.body().getCharacterDetail().getName();
                    setCharacterNameText(binding.textScoreOrRating, mName);

                    String mFavorites = String.valueOf(response.body().getCharacterDetail().getFavorites());
                    if(!mFavorites.equalsIgnoreCase("0")){
                        setCharacterScoreText(binding.textEpisodeOrVolume, mFavorites);
                    } else {
                        setCharacterScoreText(binding.textEpisodeOrVolume, "No Favorites Yet !!!");
                    }

                    String mUrl = response.body().getCharacterDetail().getUrl();
                    if(mUrl != null) {
                        setCharacterUrlText(binding.textReleaseYear, mUrl);
                    } else {
                        setCharacterUrlText(binding.textReleaseYear, "No Character URL Yet !!!");
                    }

                    binding.titleSynopsis.setText("About");
                    String mAbout = response.body().getCharacterDetail().getAbout();
                    if(mAbout != null){
                        setHtmlText(binding.textSynopsis, mAbout);
                    } else {
                        setHtmlText(binding.textSynopsis, "No Character Description Yet !!!");
                    }

                    binding.textGenreList.setText("Anime Debut");
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
        binding.rvGenreList.setAdapter(roleAdapter);

        getCharacterRole();
    }

    private void getCharacterRole() {
        Call<CharacterResponseDetail> call = apiService.getCharacterDetail(id);
        call.enqueue(new Callback<CharacterResponseDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CharacterResponseDetail> call, @NonNull Response<CharacterResponseDetail> response) {
                assert response.body() != null;
                if(response.body().getCharacterDetail().getRoleResults() !=null){
                    int oldCount = characterRole.size();
                    characterRole.addAll(response.body().getCharacterDetail().getRoleResults());
                    roleAdapter.notifyItemChanged(oldCount, characterRole.size());
                } else {
                    binding.textGenreList.setText("No Debut Yet !!!");
                    binding.rvGenreList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponseDetail> call, @NonNull Throwable t) {
                binding.loadingDetail.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this,"Fail to Fetch The Role !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setHtmlText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml(textValue, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setYearText(TextView tv, String aired, String year){
        tv.setText(HtmlCompat.fromHtml("Aired : " + aired + " " + year, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setScoresText(TextView tv, String score){
        tv.setText(HtmlCompat.fromHtml("Score : " + score, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setEpisodesText(TextView tv, String type, String episodes){
        tv.setText(HtmlCompat.fromHtml(type + " (" + episodes + " Episodes)", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setStatusText(TextView tv, String status){
        tv.setText(HtmlCompat.fromHtml("Status : " + status, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterNameText(TextView tv, String name){
        tv.setText(HtmlCompat.fromHtml("Name : " + name, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setVolumeText(TextView tv, String volume){
        tv.setText(HtmlCompat.fromHtml("Total Volume : " + volume + " Volume", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterScoreText(TextView tv, String favorites){
        tv.setText(HtmlCompat.fromHtml("Favorites : " + favorites, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterUrlText(TextView tv, String url){
        tv.setText(HtmlCompat.fromHtml("URL : " + url, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}