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
import com.example.myanimedata.adapter.CharacterAdapter;
import com.example.myanimedata.api.ApiClient;
import com.example.myanimedata.api.ApiService;
import com.example.myanimedata.api.CharacterResponse;
import com.example.myanimedata.api.CharacterResult;
import com.example.myanimedata.databinding.FragmentTopRatedCharacterBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopRatedCharacterFragment extends Fragment {
    private ApiService apiService;
    private FragmentTopRatedCharacterBinding binding;
    private CharacterAdapter topCharacterAdapter;

    private final List<CharacterResult> topCharacterResults = new ArrayList<>();

    private int page = 1;

    public TopRatedCharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTopRatedCharacterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvTopCharacterList = root.findViewById(R.id.rvTopCharacterList);
        topCharacterAdapter = new CharacterAdapter(topCharacterResults, getContext());

        rvTopCharacterList.setLayoutManager(mLayoutManager);
        rvTopCharacterList.setAdapter(topCharacterAdapter);
        getTopCharacterData(page);

        rvTopCharacterList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getTopCharacterData(page);
                }
            }
        });

        return root;
    }

    private void getTopCharacterData(int page) {
        int limit = 15;

        Call<CharacterResponse> call = apiService.getTopCharacter(page, limit);
        call.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(@NonNull Call<CharacterResponse> call, @NonNull Response<CharacterResponse> response) {
                if(response.body() != null){
                    int oldCount = topCharacterResults.size();
                    binding.loadingTopCharacter.setVisibility(View.GONE);
                    binding.rvTopCharacterList.setVisibility(View.VISIBLE);

                    topCharacterResults.addAll(response.body().getCharacterResults());
                    topCharacterAdapter.notifyItemRangeInserted(oldCount, topCharacterResults.size());
                } else if (topCharacterResults.isEmpty()) {
                    binding.loadingTopCharacter.setVisibility(View.GONE);
                    binding.textNoTopResult.setVisibility(View.VISIBLE);
                } else {
                    binding.loadingTopCharacter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponse> call, @NonNull Throwable t) {
                binding.loadingTopCharacter.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}