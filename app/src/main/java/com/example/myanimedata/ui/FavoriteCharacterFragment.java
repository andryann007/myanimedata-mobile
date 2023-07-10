package com.example.myanimedata.ui;

import android.annotation.SuppressLint;
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
import com.example.myanimedata.databinding.FragmentFavoriteCharacterBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoriteCharacterFragment extends Fragment {
    private ApiService apiService;
    private FragmentFavoriteCharacterBinding binding;
    private CharacterAdapter favoriteCharacterAdapter;

    private int page = 1;

    private final List<CharacterResult> favoriteCharacterResults = new ArrayList<>();

    public FavoriteCharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteCharacterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvFavoriteCharacterList = root.findViewById(R.id.rvFavoriteCharacterList);
        favoriteCharacterAdapter = new CharacterAdapter(favoriteCharacterResults, getContext());

        rvFavoriteCharacterList.setLayoutManager(mLayoutManager);
        rvFavoriteCharacterList.setAdapter(favoriteCharacterAdapter);
        getFavoriteCharacterData(page);

        rvFavoriteCharacterList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getFavoriteCharacterData(page);
                }
            }
        });

        return root;
    }

    private void getFavoriteCharacterData(int page) {
        int limit = 15;
        String orderBy = "favorites";
        String sort = "desc";

        Call<CharacterResponse> call = apiService.getFavoriteCharacter(page, limit, orderBy, sort);
        call.enqueue(new Callback<CharacterResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<CharacterResponse> call, @NonNull Response<CharacterResponse> response) {
                if(response.body() != null){
                    int oldCount = favoriteCharacterResults.size();
                    binding.loadingFavoriteCharacter.setVisibility(View.GONE);
                    binding.rvFavoriteCharacterList.setVisibility(View.VISIBLE);

                    favoriteCharacterResults.addAll(response.body().getCharacterResults());
                    favoriteCharacterAdapter.notifyItemRangeInserted(oldCount, favoriteCharacterResults.size());
                } else if(favoriteCharacterResults.isEmpty()) {
                    binding.loadingFavoriteCharacter.setVisibility(View.GONE);
                    binding.textNoFavoriteResult.setVisibility(View.VISIBLE);
                } else {
                    binding.loadingFavoriteCharacter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponse> call, @NonNull Throwable t) {
                binding.loadingFavoriteCharacter.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage() + " cause : "
                        + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}