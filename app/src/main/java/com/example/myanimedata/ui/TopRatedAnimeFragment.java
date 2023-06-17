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
import com.example.myanimedata.databinding.FragmentTopRatedAnimeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopRatedAnimeFragment extends Fragment {
    private ApiService apiService;
    private FragmentTopRatedAnimeBinding binding;
    private AnimeAdapter topRatedAnimeAdapter;

    private final List<AnimeResult> topRatedAnimeResults = new ArrayList<>();

    private int page = 1;

    public TopRatedAnimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTopRatedAnimeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvTopRatedAnimeList = root.findViewById(R.id.rvTopRatedAnimeList);
        topRatedAnimeAdapter = new AnimeAdapter(topRatedAnimeResults, getContext());

        rvTopRatedAnimeList.setLayoutManager(mLayoutManager);
        rvTopRatedAnimeList.setAdapter(topRatedAnimeAdapter);
        getTopRatedAnimeData(page);

        rvTopRatedAnimeList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getTopRatedAnimeData(page);
                }
            }
        });

        return root;
    }

    private void getTopRatedAnimeData(int page) {
        int limit = 15;
        String order_by = "score";

        Call<AnimeResponse> call = apiService.getTopRatedAnime(page, limit, order_by);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    int oldCount = topRatedAnimeResults.size();
                    binding.loadingTopRatedAnime.setVisibility(View.GONE);
                    binding.rvTopRatedAnimeList.setVisibility(View.VISIBLE);

                    topRatedAnimeResults.addAll(response.body().getAnimeResults());
                    topRatedAnimeAdapter.notifyItemRangeInserted(oldCount, topRatedAnimeResults.size());
                } else {
                    binding.loadingTopRatedAnime.setVisibility(View.GONE);
                    binding.textNoTopRatedResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                binding.loadingTopRatedAnime.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}