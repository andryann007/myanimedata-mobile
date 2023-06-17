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
import com.example.myanimedata.databinding.FragmentAllMangaBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllMangaFragment extends Fragment {
    private ApiService apiService;
    private FragmentAllMangaBinding binding;
    private MangaAdapter allMangaAdapter;

    private final List<MangaResult> allMangaResults = new ArrayList<>();

    private int page = 1;

    public AllMangaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllMangaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvAllMangaList = root.findViewById(R.id.rvAllMangaList);
        allMangaAdapter = new MangaAdapter(allMangaResults, getContext());

        rvAllMangaList.setLayoutManager(mLayoutManager);
        rvAllMangaList.setAdapter(allMangaAdapter);
        getAllMangaData(page);

        rvAllMangaList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getAllMangaData(page);
                }
            }
        });

        return root;
    }

    private void getAllMangaData(int page) {
        int limit = 1;

        Call<MangaResponse> call = apiService.getAllManga(page, limit);
        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if(response.body() != null){
                    int oldCount = allMangaResults.size();
                    binding.loadingAllManga.setVisibility(View.GONE);
                    binding.rvAllMangaList.setVisibility(View.VISIBLE);

                    allMangaResults.addAll(response.body().getMangaResults());
                    allMangaAdapter.notifyItemRangeInserted(oldCount, allMangaResults.size());
                } else {
                    binding.loadingAllManga.setVisibility(View.GONE);
                    binding.textNoAllResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                binding.loadingAllManga.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}