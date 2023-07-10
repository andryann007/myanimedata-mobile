package com.example.myanimedata.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.adapter.AnimeAdapter;
import com.example.myanimedata.adapter.CharacterAdapter;
import com.example.myanimedata.adapter.MangaAdapter;
import com.example.myanimedata.api.AnimeResponse;
import com.example.myanimedata.api.AnimeResult;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;
import com.example.myanimedata.api.CharacterResponse;
import com.example.myanimedata.api.CharacterResult;
import com.example.myanimedata.api.MangaResponse;
import com.example.myanimedata.api.MangaResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {
    private ApiService apiService;
    private AnimeAdapter searchAnimeAdapter;
    private MangaAdapter searchMangaAdapter;
    private CharacterAdapter searchCharacterAdapter;

    private final List<AnimeResult> searchAnimeResults = new ArrayList<>();
    private final List<MangaResult> searchMangaResults = new ArrayList<>();
    private final List<CharacterResult> searchCharacterResults = new ArrayList<>();

    private TextView noSearchResult;
    private RecyclerView rvSearch;

    private ProgressBar progressSearch;

    private int page = 1;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        query = getIntent().getStringExtra("q");
        String type = getIntent().getStringExtra("type");

        Toolbar searchToolbar = findViewById(R.id.searchToolbar);
        TextView searchResult = findViewById(R.id.textSearchResult);
        noSearchResult = findViewById(R.id.textNoSearchResult);
        rvSearch = findViewById(R.id.rvSearchList);
        progressSearch = findViewById(R.id.loadingSearch);

        setSupportActionBar(searchToolbar);
        setSearchText(searchResult, query);

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);

        rvSearch.setLayoutManager(mLayoutManager);

        switch (type) {
            case "anime":
                searchAnimeAdapter = new AnimeAdapter(searchAnimeResults, this);
                rvSearch.setAdapter(searchAnimeAdapter);
                searchAnimeData(page);

                rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                        super.onScrolled(recyclerView, dx, dy);

                        if(!recyclerView.canScrollVertically(1)){
                            page++;
                            searchAnimeData(page);
                        }
                    }
                });
                break;

            case "manga":
                searchMangaAdapter = new MangaAdapter(searchMangaResults, this);
                rvSearch.setAdapter(searchMangaAdapter);
                searchMangaData(page);

                rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                        super.onScrolled(recyclerView, dx, dy);

                        if(!recyclerView.canScrollVertically(1)){
                            page++;
                            searchMangaData(page);
                        }
                    }
                });
                break;

            case "character":
                searchCharacterAdapter = new CharacterAdapter(searchCharacterResults, this);
                rvSearch.setAdapter(searchCharacterAdapter);
                searchCharacterData(page);

                rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                        super.onScrolled(recyclerView, dx, dy);

                        if(!recyclerView.canScrollVertically(1)){
                            page++;
                            searchCharacterData(page);
                        }
                    }
                });
                break;
        }

        searchToolbar.setOnClickListener(v->onBackPressed());
    }

    private void searchAnimeData(int page) {
        int limit = 15;

        Call<AnimeResponse> call = apiService.searchAllAnime(page, limit, query);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    int oldCount = searchAnimeResults.size();
                    progressSearch.setVisibility(View.GONE);
                    rvSearch.setVisibility(View.VISIBLE);

                    searchAnimeResults.addAll(response.body().getAnimeResults());
                    searchAnimeAdapter.notifyItemRangeInserted(oldCount, searchAnimeResults.size());
                } else if(searchAnimeResults.isEmpty()) {
                    progressSearch.setVisibility(View.GONE);
                    noSearchResult.setVisibility(View.VISIBLE);
                } else {
                    progressSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressSearch.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchMangaData(int page) {
        int limit = 15;

        Call<MangaResponse> call = apiService.searchAllManga(page, limit, query);
        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if(response.body() != null){
                    int oldCount = searchMangaResults.size();
                    progressSearch.setVisibility(View.GONE);
                    rvSearch.setVisibility(View.VISIBLE);

                    searchMangaResults.addAll(response.body().getMangaResults());
                    searchMangaAdapter.notifyItemRangeInserted(oldCount, searchMangaResults.size());
                } else if(searchMangaResults.isEmpty()){
                    progressSearch.setVisibility(View.GONE);
                    noSearchResult.setVisibility(View.VISIBLE);
                } else {
                    progressSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                progressSearch.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchCharacterData(int page) {
        int limit = 15;

        Call<CharacterResponse> call = apiService.searchAllCharacter(page, limit, query);
        call.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(@NonNull Call<CharacterResponse> call, @NonNull Response<CharacterResponse> response) {
                if(response.body() != null){
                    int oldCount = searchCharacterResults.size();
                    progressSearch.setVisibility(View.GONE);
                    rvSearch.setVisibility(View.VISIBLE);

                    searchCharacterResults.addAll(response.body().getCharacterResults());
                    searchCharacterAdapter.notifyItemRangeInserted(oldCount, searchCharacterResults.size());
                } else if(searchCharacterResults.isEmpty()) {
                    progressSearch.setVisibility(View.GONE);
                    noSearchResult.setVisibility(View.VISIBLE);
                } else {
                    progressSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponse> call, @NonNull Throwable t) {
                progressSearch.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSearchText(TextView tv, String textValue){
        tv.setText(HtmlCompat.fromHtml("Search Result For : <b>' " + textValue + " '</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}