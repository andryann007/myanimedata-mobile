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
import com.example.myanimedata.databinding.FragmentAllAnimeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllAnimeFragment extends Fragment {
    private ApiService apiService;
    private FragmentAllAnimeBinding binding;
    private AnimeAdapter allAnimeAdapter;

    private final List<AnimeResult> allAnimeResults = new ArrayList<>();

    private int page = 1;

    public AllAnimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllAnimeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvAllAnimeList = root.findViewById(R.id.rvAllAnimeList);
        allAnimeAdapter = new AnimeAdapter(allAnimeResults, getContext());

        rvAllAnimeList.setLayoutManager(mLayoutManager);
        rvAllAnimeList.setAdapter(allAnimeAdapter);
        getAllAnimeData(page);

        rvAllAnimeList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getAllAnimeData(page);
                }
            }
        });

        return root;
    }

    private void getAllAnimeData(int page) {
        int limit = 1;

        Call<AnimeResponse> call = apiService.getAllAnime(page, limit);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if(response.body() != null){
                    int oldCount = allAnimeResults.size();
                    binding.loadingAllAnime.setVisibility(View.GONE);
                    binding.rvAllAnimeList.setVisibility(View.VISIBLE);

                    allAnimeResults.addAll(response.body().getAnimeResults());
                    allAnimeAdapter.notifyItemRangeInserted(oldCount, allAnimeResults.size());
                } else {
                    binding.loadingAllAnime.setVisibility(View.GONE);
                    binding.textNoAllResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                binding.loadingAllAnime.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}