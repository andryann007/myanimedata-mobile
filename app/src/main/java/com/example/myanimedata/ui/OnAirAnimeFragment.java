package com.example.myanimedata.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.adapter.AnimeAdapter;
import com.example.myanimedata.api.AnimeResponse;
import com.example.myanimedata.api.AnimeResult;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;
import com.example.myanimedata.databinding.FragmentOnAirAnimeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnAirAnimeFragment extends Fragment {
    private ApiService apiService;
    private FragmentOnAirAnimeBinding binding;
    private AnimeAdapter onAirAnimeAdapter;

    private final List<AnimeResult> onAirAnimeResults = new ArrayList<>();

    private int page = 1;

    public OnAirAnimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOnAirAnimeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvOnAirAnimeList = root.findViewById(R.id.rvOnAirAnimeList);
        onAirAnimeAdapter = new AnimeAdapter(onAirAnimeResults, getContext());

        rvOnAirAnimeList.setLayoutManager(mLayoutManager);
        rvOnAirAnimeList.setAdapter(onAirAnimeAdapter);
        getOnAirAnimeData(page);

        rvOnAirAnimeList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getOnAirAnimeData(page);
                }
            }
        });

        return root;
    }

    private void getOnAirAnimeData(int page) {
        int limit = 15;
        String status = "airing";
        String order_by = "popularity";

        Call<AnimeResponse> call = apiService.getAiringAnime(page, limit, status, order_by);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    int oldCount = onAirAnimeResults.size();
                    binding.loadingOnAirAnime.setVisibility(View.GONE);
                    binding.rvOnAirAnimeList.setVisibility(View.VISIBLE);

                    onAirAnimeResults.addAll(response.body().getAnimeResults());
                    onAirAnimeAdapter.notifyItemRangeInserted(oldCount, onAirAnimeResults.size());
                } else {
                    binding.loadingOnAirAnime.setVisibility(View.GONE);
                    binding.textNoOnAirResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                binding.loadingOnAirAnime.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}