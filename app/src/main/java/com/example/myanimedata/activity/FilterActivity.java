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
    private AnimeAdapter filterAnimeAdapter;
    private final List<AnimeResult> filterAnimeResults = new ArrayList<>();

    private TextView noFilterResult;
    private RecyclerView rvFilter;

    private ProgressBar progressFilter;

    private int page = 1;
    private String type = null;
    private String status = null;
    private String rating = null;
    private String orderBy = null;
    private String sortType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        type = getIntent().getStringExtra("type");
        status = getIntent().getStringExtra("status");
        rating = getIntent().getStringExtra("rating");
        orderBy = getIntent().getStringExtra("order_by");
        sortType = getIntent().getStringExtra("sort");

        String sortName;

        if(sortType.equalsIgnoreCase("asc")){
            sortName = "Ascending";
        } else {
            sortName = "Descending";
        }

        Toolbar filterToolbar = findViewById(R.id.filterToolbar);
        TextView filterResult = findViewById(R.id.textFilterResult);
        TextView animeType = findViewById(R.id.textFilterByType);
        TextView animeStatus = findViewById(R.id.textFilterByStatus);
        TextView animeRating = findViewById(R.id.textFilterByRating);
        TextView animeOrderBy = findViewById(R.id.textFilterByOrderType);
        noFilterResult = findViewById(R.id.textNoFilterResult);
        rvFilter = findViewById(R.id.rvFilterList);
        progressFilter = findViewById(R.id.loadingFilter);

        filterToolbar.setTitle("Filter Result (" + sortName + ") :");
        setSupportActionBar(filterToolbar);

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        setFilterText(filterResult, type);
        setTypeText(animeType, type);
        setStatusText(animeStatus, status);
        setRatingText(animeRating, rating);
        setOrderByText(animeOrderBy, orderBy);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);

        rvFilter.setLayoutManager(mLayoutManager);

        filterAnimeAdapter = new AnimeAdapter(filterAnimeResults, this);
        rvFilter.setAdapter(filterAnimeAdapter);
        filterAnimeData(page);

        rvFilter.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    filterAnimeData(page);
                }
            }
        });

        filterToolbar.setOnClickListener(v->onBackPressed());
    }

    private void filterAnimeData(int page) {
        int limit = 15;

        Call<AnimeResponse> call = apiService.filterAnime(page, limit, type, status, rating, orderBy, sortType);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    int oldCount = filterAnimeResults.size();
                    progressFilter.setVisibility(View.GONE);
                    rvFilter.setVisibility(View.VISIBLE);

                    filterAnimeResults.addAll(response.body().getAnimeResults());
                    filterAnimeAdapter.notifyItemRangeInserted(oldCount, filterAnimeResults.size());
                } else if(filterAnimeResults.isEmpty()) {
                    progressFilter.setVisibility(View.GONE);
                    noFilterResult.setVisibility(View.VISIBLE);
                } else {
                    progressFilter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                progressFilter.setVisibility(View.GONE);
                Toast.makeText(FilterActivity.this, "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFilterText(TextView tv, String type) {
        tv.setText(HtmlCompat.fromHtml("Filter Anime Type For : <b>'" + type + "'</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setTypeText(TextView tv, String type) {
        tv.setText(HtmlCompat.fromHtml("Anime Type : <b>'" + type + "'</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setStatusText(TextView tv, String status) {
        tv.setText(HtmlCompat.fromHtml("Anime Status : <b>'" + status + "'</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setRatingText(TextView tv, String rating) {
        tv.setText(HtmlCompat.fromHtml("Anime Rating : <b>'" + rating + "'</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setOrderByText(TextView tv, String orderBy){
        tv.setText(HtmlCompat.fromHtml("Order By : <b>'" + orderBy + "'</b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}