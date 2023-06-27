package com.example.myanimedata.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
                    setYearText(binding.textReleaseYear, mSeason, mYear);

                    String mScore = String.valueOf(response.body().getAnimeDetails().getScore());
                    setScoresText(binding.textScoreOrRating, mScore);

                    String mType = response.body().getAnimeDetails().getType();
                    String mEpisodes = String.valueOf(response.body().getAnimeDetails().getEpisodes());
                    setEpisodesText(binding.textEpisodeOrVolume, mType, mEpisodes);

                    String mSynopsis = response.body().getAnimeDetails().getSynopsis();
                    setHtmlText(binding.textSynopsis, mSynopsis);

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
            @Override
            public void onResponse(@NonNull Call<AnimeResponseDetail> call, @NonNull Response<AnimeResponseDetail> response) {
                assert response.body() != null;
                if(response.body().getAnimeDetails().getGenreResults() !=null){
                    int oldCount = animeGenre.size();
                    animeGenre.addAll(response.body().getAnimeDetails().getGenreResults());
                    genreAdapter.notifyItemChanged(oldCount, animeGenre.size());
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
                    setStatusText(binding.textReleaseYear, mStatus);

                    String mScore = String.valueOf(response.body().getMangaDetails().getScore());
                    setScoresText(binding.textScoreOrRating, mScore);

                    String mVolume = String.valueOf(response.body().getMangaDetails().getVolumes());
                    setVolumeText(binding.textEpisodeOrVolume, mVolume);

                    String mSynopsis = response.body().getMangaDetails().getSynopsis();
                    setHtmlText(binding.textSynopsis, mSynopsis);

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
            @Override
            public void onResponse(@NonNull Call<MangaResponseDetail> call, @NonNull Response<MangaResponseDetail> response) {
                assert response.body() != null;
                if(response.body().getMangaDetails().getGenreResults() != null){
                    int oldCount = mangaGenre.size();
                    mangaGenre.addAll(response.body().getMangaDetails().getGenreResults());
                    genreAdapter.notifyItemChanged(oldCount, mangaGenre.size());
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

                    String mStatus = response.body().getCharacterDetail().getName();
                    setCharacterNameText(binding.textScoreOrRating, mStatus);

                    String mScore = String.valueOf(response.body().getCharacterDetail().getFavorites());
                    setCharacterScoreText(binding.textEpisodeOrVolume, mScore);

                    String mVolume = String.valueOf(response.body().getCharacterDetail().getUrl());
                    setCharacterUrlText(binding.textReleaseYear, mVolume);

                    binding.titleSynopsis.setText("About");
                    String mAbout = response.body().getCharacterDetail().getAbout();
                    setHtmlText(binding.textSynopsis, mAbout);

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
            @Override
            public void onResponse(@NonNull Call<CharacterResponseDetail> call, @NonNull Response<CharacterResponseDetail> response) {
                assert response.body() != null;
                if(response.body().getCharacterDetail().getRoleResults() !=null){
                    int oldCount = characterRole.size();
                    characterRole.addAll(response.body().getCharacterDetail().getRoleResults());
                    roleAdapter.notifyItemChanged(oldCount, characterRole.size());
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

    private void setYearText(TextView tv, String type, String year){
        tv.setText(HtmlCompat.fromHtml("Aired : " + type + " " + year, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setScoresText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml("Score : " + textValue, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setEpisodesText(TextView tv, String type, String episodes){
        tv.setText(HtmlCompat.fromHtml(type + " (" + episodes + " Episodes)", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setStatusText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml("Status : " + textValue, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterNameText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml("Name : " + textValue, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setVolumeText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml("Total Volume : " + textValue + " Volume", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterScoreText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml("Favorites : " + textValue, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setCharacterUrlText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml("URL : " + textValue, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}