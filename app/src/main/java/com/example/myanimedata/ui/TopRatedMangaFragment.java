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
import com.example.myanimedata.databinding.FragmentTopRatedMangaBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopRatedMangaFragment extends Fragment {
    private ApiService apiService;
    private FragmentTopRatedMangaBinding binding;
    private MangaAdapter topRatedMangaAdapter;

    private final List<MangaResult> topRatedMangaResults = new ArrayList<>();

    private int page = 1;

    public TopRatedMangaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTopRatedMangaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvTopRatedMangaList = root.findViewById(R.id.rvTopRatedMangaList);
        topRatedMangaAdapter = new MangaAdapter(topRatedMangaResults, getContext());

        rvTopRatedMangaList.setLayoutManager(mLayoutManager);
        rvTopRatedMangaList.setAdapter(topRatedMangaAdapter);
        getTopRatedMangaData(page);

        rvTopRatedMangaList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getTopRatedMangaData(page);
                }
            }
        });

        return root;
    }

    private void getTopRatedMangaData(int page) {
        int limit = 15;
        String status = "complete";
        String order_by = "score";

        Call<MangaResponse> call = apiService.getTopRatedManga(page, limit, status, order_by);
        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if(response.body() != null){
                    int oldCount = topRatedMangaResults.size();
                    binding.loadingTopRatedManga.setVisibility(View.GONE);
                    binding.rvTopRatedMangaList.setVisibility(View.VISIBLE);

                    topRatedMangaResults.addAll(response.body().getMangaResults());
                    topRatedMangaAdapter.notifyItemRangeInserted(oldCount, topRatedMangaResults.size());
                } else {
                    binding.loadingTopRatedManga.setVisibility(View.GONE);
                    binding.textNoTopRatedResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                binding.loadingTopRatedManga.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}