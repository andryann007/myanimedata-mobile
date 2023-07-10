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
import com.example.myanimedata.adapter.MangaAdapter;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;
import com.example.myanimedata.api.MangaResponse;
import com.example.myanimedata.api.MangaResult;
import com.example.myanimedata.databinding.FragmentPopularMangaBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PopularMangaFragment extends Fragment {
    private ApiService apiService;
    private FragmentPopularMangaBinding binding;
    private MangaAdapter popularMangaAdapter;

    private final List<MangaResult> popularMangaResults = new ArrayList<>();

    private int page = 1;

    public PopularMangaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPopularMangaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvPopularMangaList = root.findViewById(R.id.rvPopularMangaList);
        popularMangaAdapter = new MangaAdapter(popularMangaResults, getContext());

        rvPopularMangaList.setLayoutManager(mLayoutManager);
        rvPopularMangaList.setAdapter(popularMangaAdapter);
        getPopularMangaData(page);

        rvPopularMangaList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getPopularMangaData(page);
                }
            }
        });

        return root;
    }

    private void getPopularMangaData(int page) {
        int limit = 15;
        String orderBy = "popularity";
        String sort = "desc";
        String startDate = "2000-01-01";
        String status = "complete";

        Call<MangaResponse> call = apiService.getPopularManga(page, limit, orderBy, sort, startDate, status);
        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if(response.body() != null){
                    int oldCount = popularMangaResults.size();
                    binding.loadingPopularManga.setVisibility(View.GONE);
                    binding.rvPopularMangaList.setVisibility(View.VISIBLE);

                    popularMangaResults.addAll(response.body().getMangaResults());
                    popularMangaAdapter.notifyItemRangeInserted(oldCount, popularMangaResults.size());
                } else if(popularMangaResults.isEmpty()) {
                    binding.loadingPopularManga.setVisibility(View.GONE);
                    binding.textNoPopularResult.setVisibility(View.VISIBLE);
                } else {
                    binding.loadingPopularManga.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                binding.loadingPopularManga.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}