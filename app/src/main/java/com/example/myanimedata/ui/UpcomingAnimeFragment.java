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
import com.example.myanimedata.databinding.FragmentUpcomingAnimeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpcomingAnimeFragment extends Fragment {
    private ApiService apiService;
    private FragmentUpcomingAnimeBinding binding;
    private AnimeAdapter upcomingAnimeAdapter;

    private final List<AnimeResult> upcomingAnimeResults = new ArrayList<>();

    private int page = 1;

    public UpcomingAnimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingAnimeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvUpcomingAnimeList = root.findViewById(R.id.rvUpcomingAnimeList);
        upcomingAnimeAdapter = new AnimeAdapter(upcomingAnimeResults, getContext());

        rvUpcomingAnimeList.setLayoutManager(mLayoutManager);
        rvUpcomingAnimeList.setAdapter(upcomingAnimeAdapter);
        getUpcomingAnimeData(page);

        rvUpcomingAnimeList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getUpcomingAnimeData(page);
                }
            }
        });

        return root;
    }

    private void getUpcomingAnimeData(int page) {
        int limit = 15;
        String status = "upcoming";

        Call<AnimeResponse> call = apiService.getUpcomingAnime(page, limit, status);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    int oldCount = upcomingAnimeResults.size();
                    binding.loadingUpcomingAnime.setVisibility(View.GONE);
                    binding.rvUpcomingAnimeList.setVisibility(View.VISIBLE);

                    upcomingAnimeResults.addAll(response.body().getAnimeResults());
                    upcomingAnimeAdapter.notifyItemRangeInserted(oldCount, upcomingAnimeResults.size());
                } else {
                    binding.loadingUpcomingAnime.setVisibility(View.GONE);
                    binding.textNoUpcomingResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                binding.loadingUpcomingAnime.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}