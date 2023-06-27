package com.example.myanimedata.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myanimedata.R;
import com.example.myanimedata.adapter.AnimeAdapter;
import com.example.myanimedata.api.AnimeResponse;
import com.example.myanimedata.api.AnimeResult;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;
import com.example.myanimedata.databinding.FragmentPopularAnimeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PopularAnimeFragment extends Fragment {
    private ApiService apiService;
    private FragmentPopularAnimeBinding binding;
    private AnimeAdapter popularAnimeAdapter;

    private final List<AnimeResult> popularAnimeResults = new ArrayList<>();

    private int page = 1;

    public PopularAnimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPopularAnimeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvPopularAnimeList = root.findViewById(R.id.rvPopularAnimeList);
        popularAnimeAdapter = new AnimeAdapter(popularAnimeResults, getContext());

        rvPopularAnimeList.setLayoutManager(mLayoutManager);
        rvPopularAnimeList.setAdapter(popularAnimeAdapter);
        getPopularAnimeData(page);

        rvPopularAnimeList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getPopularAnimeData(page);
                }
            }
        });

        return root;
    }

    private void getPopularAnimeData(int page) {
        int limit = 15;
        String orderBy = "popularity";
        String sort = "desc";
        String startDate = "2023-01-01";
        int minScore = 7;

        Call<AnimeResponse> call = apiService.getPopularAnime(page, limit, orderBy, sort, startDate, minScore);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    int oldCount = popularAnimeResults.size();
                    binding.loadingPopularAnime.setVisibility(View.GONE);
                    binding.rvPopularAnimeList.setVisibility(View.VISIBLE);

                    popularAnimeResults.addAll(response.body().getAnimeResults());
                    popularAnimeAdapter.notifyItemRangeInserted(oldCount, popularAnimeResults.size());
                } else if(popularAnimeResults.isEmpty()) {
                    binding.loadingPopularAnime.setVisibility(View.GONE);
                    binding.textNoPopularResult.setVisibility(View.VISIBLE);
                } else {
                    binding.loadingPopularAnime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                binding.loadingPopularAnime.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}