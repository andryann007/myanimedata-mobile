package com.example.myanimedata.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myanimedata.R;
import com.example.myanimedata.adapter.AnimeAdapter;
import com.example.myanimedata.api.AnimeResponse;
import com.example.myanimedata.api.AnimeResult;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FilterActivity extends AppCompatActivity {

    private ApiService apiService;
    private AnimeAdapter filterAnimeAdapter, filterAnimeTypeAdapter, filterAnimeRatingAdapter,
            filterAnimeStatusAdapter, filterAnimeTypeAndRatingAdapter, filterAnimeTypeAndStatusAdapter,
            filterAnimeStatusAndRatingAdapter;
    private final List<AnimeResult> filterAnimeResults = new ArrayList<>();
    private final List<AnimeResult> filterAnimeType = new ArrayList<>();
    private final List<AnimeResult> filterAnimeRating = new ArrayList<>();
    private final List<AnimeResult> filterAnimeStatus = new ArrayList<>();
    private final List<AnimeResult> filterAnimeTypeAndRating = new ArrayList<>();
    private final List<AnimeResult> filterAnimeTypeAndStatus = new ArrayList<>();
    private final List<AnimeResult> filterAnimeStatusAndRating = new ArrayList<>();

    private TextView noFilterResult;
    private RecyclerView rvFilter;

    private ProgressBar progressFilter;

    private int page = 1;
    private final int limit = 15;
    private String type = null;
    private String status = null;
    private String rating = null;
    private String orderBy = null;
    private String sortType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        String filterType = getIntent().getStringExtra("filter_type");

        Toolbar filterToolbar = findViewById(R.id.filterToolbar);
        TextView filterResult = findViewById(R.id.textFilterResult);
        TextView animeType = findViewById(R.id.textFilterByType);
        TextView animeStatus = findViewById(R.id.textFilterByStatus);
        TextView animeRating = findViewById(R.id.textFilterByRating);
        TextView animeOrderBy = findViewById(R.id.textFilterByOrderType);
        noFilterResult = findViewById(R.id.textNoFilterResult);
        rvFilter = findViewById(R.id.rvFilterList);
        progressFilter = findViewById(R.id.loadingFilter);

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);

        rvFilter.setLayoutManager(mLayoutManager);

        String sortName;
        switch (filterType) {
            case "filter_all":
                type = getIntent().getStringExtra("type");
                status = getIntent().getStringExtra("status");
                rating = getIntent().getStringExtra("rating");
                orderBy = getIntent().getStringExtra("order_by");
                sortType = getIntent().getStringExtra("sort");

                if(sortType.equalsIgnoreCase("asc")){
                    sortName = "ascending";
                } else {
                    sortName = "descending";
                }

                filterToolbar.setTitle("Filter Result (" + sortName + ") :");
                setSupportActionBar(filterToolbar);

                setFilterText(filterResult, type.toUpperCase());
                setTypeText(animeType, type);
                setStatusText(animeStatus, status);
                setRatingText(animeRating, rating);
                setOrderByText(animeOrderBy, orderBy);

                filterAnimeAdapter = new AnimeAdapter(filterAnimeResults, this);
                rvFilter.setAdapter(filterAnimeAdapter);
                getFilterAnimeData(page);

                rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1)) {
                            page++;
                            getFilterAnimeData(page);
                        }
                    }
                });
                break;
            case "filter_type":
                type = getIntent().getStringExtra("type");
                orderBy = getIntent().getStringExtra("order_by");
                sortType = getIntent().getStringExtra("sort");

                filterToolbar.setTitle("Filter Anime Type : (" + type.toUpperCase() + ") :");
                setSupportActionBar(filterToolbar);

                setFilterText(filterResult, type.toUpperCase());
                setTypeText(animeType, type);
                setStatusText(animeStatus, "-");
                setRatingText(animeRating, "-");
                setOrderByText(animeOrderBy, orderBy);

                filterAnimeTypeAdapter = new AnimeAdapter(filterAnimeType, this);
                rvFilter.setAdapter(filterAnimeTypeAdapter);
                getFilterAnimeType(page);

                rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1)) {
                            page++;
                            getFilterAnimeType(page);
                        }
                    }
                });
                break;
            case "filter_status":
                status = getIntent().getStringExtra("status");
                orderBy = getIntent().getStringExtra("order_by");
                sortType = getIntent().getStringExtra("sort");

                filterToolbar.setTitle("Filter Anime Status : (" + status.toUpperCase() + ") :");
                setSupportActionBar(filterToolbar);

                setFilterText(filterResult, status.toUpperCase());
                setTypeText(animeType, "-");
                setStatusText(animeStatus, status);
                setRatingText(animeRating, "-");
                setOrderByText(animeOrderBy, orderBy);

                filterAnimeStatusAdapter = new AnimeAdapter(filterAnimeStatus, this);
                rvFilter.setAdapter(filterAnimeStatusAdapter);
                getFilterAnimeStatus(page);

                rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1)) {
                            page++;
                            getFilterAnimeStatus(page);
                        }
                    }
                });
                break;
            case "filter_rating":
                rating = getIntent().getStringExtra("rating");
                orderBy = getIntent().getStringExtra("order_by");
                sortType = getIntent().getStringExtra("sort");

                filterToolbar.setTitle("Filter Anime Rating : (" + rating.toUpperCase() + ") :");
                setSupportActionBar(filterToolbar);

                setFilterText(filterResult, rating.toUpperCase());
                setTypeText(animeType, "-");
                setStatusText(animeStatus, "-");
                setRatingText(animeRating, rating);
                setOrderByText(animeOrderBy, orderBy);

                filterAnimeRatingAdapter = new AnimeAdapter(filterAnimeRating, this);
                rvFilter.setAdapter(filterAnimeRatingAdapter);
                getFilterAnimeRating(page);

                rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1)) {
                            page++;
                            getFilterAnimeRating(page);
                        }
                    }
                });
                break;
            case "filter_type_and_status":
                type = getIntent().getStringExtra("type");
                status = getIntent().getStringExtra("status");
                orderBy = getIntent().getStringExtra("order_by");
                sortType = getIntent().getStringExtra("sort");

                filterToolbar.setTitle("Filter Anime : (" + type.toUpperCase() + ", " + status.toUpperCase() + ") :");
                setSupportActionBar(filterToolbar);

                setFilterText(filterResult, type.toUpperCase());
                setTypeText(animeType, type);
                setStatusText(animeStatus, status);
                setRatingText(animeRating, "-");
                setOrderByText(animeOrderBy, orderBy);

                filterAnimeTypeAndStatusAdapter = new AnimeAdapter(filterAnimeTypeAndStatus, this);
                rvFilter.setAdapter(filterAnimeTypeAndStatusAdapter);
                getFilterAnimeTypeStatus(page);

                rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1)) {
                            page++;
                            getFilterAnimeTypeStatus(page);
                        }
                    }
                });
                break;
            case "filter_type_and_rating":
                type = getIntent().getStringExtra("type");
                rating = getIntent().getStringExtra("rating");
                orderBy = getIntent().getStringExtra("order_by");
                sortType = getIntent().getStringExtra("sort");

                filterToolbar.setTitle("Filter Anime : (" + type.toUpperCase() + ", " + rating.toUpperCase() + ") :");
                setSupportActionBar(filterToolbar);

                setFilterText(filterResult, type.toUpperCase());
                setTypeText(animeType, type);
                setStatusText(animeStatus, "-");
                setRatingText(animeRating, rating);
                setOrderByText(animeOrderBy, orderBy);

                filterAnimeTypeAndRatingAdapter = new AnimeAdapter(filterAnimeTypeAndRating, this);
                rvFilter.setAdapter(filterAnimeTypeAndRatingAdapter);
                getFilterAnimeTypeRating(page);

                rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1)) {
                            page++;
                            getFilterAnimeTypeRating(page);
                        }
                    }
                });
                break;
            case "filter_status_and_rating":
                status = getIntent().getStringExtra("status");
                rating = getIntent().getStringExtra("rating");
                orderBy = getIntent().getStringExtra("order_by");
                sortType = getIntent().getStringExtra("sort");

                filterToolbar.setTitle("Filter Anime : (" + status.toUpperCase() + ", " + rating.toUpperCase() + ") :");
                setSupportActionBar(filterToolbar);

                setFilterText(filterResult, status.toUpperCase());
                setTypeText(animeType, "-");
                setStatusText(animeStatus, status);
                setRatingText(animeRating, rating);
                setOrderByText(animeOrderBy, orderBy);

                filterAnimeStatusAndRatingAdapter = new AnimeAdapter(filterAnimeStatusAndRating, this);
                rvFilter.setAdapter(filterAnimeStatusAndRatingAdapter);
                getFilterAnimeStatusRating(page);

                rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1)) {
                            page++;
                            getFilterAnimeStatusRating(page);
                        }
                    }
                });
                break;
        }

        filterToolbar.setOnClickListener(v->onBackPressed());
    }

    private void getFilterAnimeData(int page) {
        Call<AnimeResponse> call = apiService.filterAnime(page, limit, type, status, rating, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    if(!response.body().getAnimeResults().isEmpty() && response.body().getAnimeResults().size() > 0){
                        int oldCount = filterAnimeResults.size();
                        progressFilter.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);

                        filterAnimeResults.addAll(response.body().getAnimeResults());
                        filterAnimeAdapter.notifyItemRangeInserted(oldCount, filterAnimeResults.size());
                    } else {
                        progressFilter.setVisibility(View.GONE);
                        noFilterResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilterAnimeType(int page){
        Call<AnimeResponse> call = apiService.filterAnimeType(page, limit, type, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    if(!response.body().getAnimeResults().isEmpty() && response.body().getAnimeResults().size() > 0){
                        int oldCount = filterAnimeType.size();
                        progressFilter.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);

                        filterAnimeType.addAll(response.body().getAnimeResults());
                        filterAnimeTypeAdapter.notifyItemRangeInserted(oldCount, filterAnimeType.size());
                    } else {
                        progressFilter.setVisibility(View.GONE);
                        noFilterResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilterAnimeRating(int page){
        Call<AnimeResponse> call = apiService.filterAnimeRating(page, limit, rating, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    if(!response.body().getAnimeResults().isEmpty() && response.body().getAnimeResults().size() > 0){
                        int oldCount = filterAnimeRating.size();
                        progressFilter.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);

                        filterAnimeRating.addAll(response.body().getAnimeResults());
                        filterAnimeRatingAdapter.notifyItemRangeInserted(oldCount, filterAnimeRating.size());
                    } else {
                        progressFilter.setVisibility(View.GONE);
                        noFilterResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilterAnimeStatus(int page){
        Call<AnimeResponse> call = apiService.filterAnimeStatus(page, limit, status, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    if(!response.body().getAnimeResults().isEmpty() && response.body().getAnimeResults().size() > 0){
                        int oldCount = filterAnimeStatus.size();
                        progressFilter.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);

                        filterAnimeStatus.addAll(response.body().getAnimeResults());
                        filterAnimeStatusAdapter.notifyItemRangeInserted(oldCount, filterAnimeStatus.size());
                    } else {
                        progressFilter.setVisibility(View.GONE);
                        noFilterResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilterAnimeTypeRating(int page){
        Call<AnimeResponse> call = apiService.filterAnimeTypeRating(page, limit, type, rating, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    if(!response.body().getAnimeResults().isEmpty() && response.body().getAnimeResults().size() > 0){
                        int oldCount = filterAnimeTypeAndRating.size();
                        progressFilter.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);

                        filterAnimeTypeAndRating.addAll(response.body().getAnimeResults());
                        filterAnimeTypeAndRatingAdapter.notifyItemRangeInserted(oldCount, filterAnimeTypeAndRating.size());
                    } else {
                        progressFilter.setVisibility(View.GONE);
                        noFilterResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilterAnimeTypeStatus(int page){
        Call<AnimeResponse> call = apiService.filterAnimeTypeStatus(page, limit, type, status, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    if(!response.body().getAnimeResults().isEmpty() && response.body().getAnimeResults().size() > 0){
                        int oldCount = filterAnimeTypeAndStatus.size();
                        progressFilter.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);

                        filterAnimeTypeAndStatus.addAll(response.body().getAnimeResults());
                        filterAnimeTypeAndStatusAdapter.notifyItemRangeInserted(oldCount, filterAnimeTypeAndStatus.size());
                    } else {
                        progressFilter.setVisibility(View.GONE);
                        noFilterResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilterAnimeStatusRating(int page){
        Call<AnimeResponse> call = apiService.filterAnimeStatusRating(page, limit, status, rating, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    if(!response.body().getAnimeResults().isEmpty() && response.body().getAnimeResults().size() > 0){
                        int oldCount = filterAnimeStatusAndRating.size();
                        progressFilter.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);

                        filterAnimeStatusAndRating.addAll(response.body().getAnimeResults());
                        filterAnimeStatusAndRatingAdapter.notifyItemRangeInserted(oldCount, filterAnimeStatusAndRating.size());
                    } else {
                        progressFilter.setVisibility(View.GONE);
                        noFilterResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFilterText(TextView tv, String type) {
        tv.setText(HtmlCompat.fromHtml("Filter Anime Type For : <b>'" + type + "'</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setTypeText(TextView tv, String type) {
        tv.setText(HtmlCompat.fromHtml("Anime Type : <b>" + type + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setStatusText(TextView tv, String status) {
        tv.setText(HtmlCompat.fromHtml("Anime Status : <b>" + status + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setRatingText(TextView tv, String rating) {
        tv.setText(HtmlCompat.fromHtml("Anime Rating : <b>" + rating + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setOrderByText(TextView tv, String orderBy){
        tv.setText(HtmlCompat.fromHtml("Order By : <b>" + orderBy + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}