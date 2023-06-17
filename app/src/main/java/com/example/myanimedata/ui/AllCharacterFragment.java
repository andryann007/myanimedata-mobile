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
import com.example.myanimedata.databinding.FragmentAllCharacterBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllCharacterFragment extends Fragment {
    private ApiService apiService;
    private FragmentAllCharacterBinding binding;
    private CharacterAdapter allCharacterAdapter;

    private final List<CharacterResult> allCharacterResults = new ArrayList<>();

    private int page = 1;

    public AllCharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllCharacterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        RecyclerView rvAllCharacterList = root.findViewById(R.id.rvAllCharacterList);
        allCharacterAdapter = new CharacterAdapter(allCharacterResults, getContext());

        rvAllCharacterList.setLayoutManager(mLayoutManager);
        rvAllCharacterList.setAdapter(allCharacterAdapter);
        getAllCharacterData(page);

        rvAllCharacterList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getAllCharacterData(page);
                }
            }
        });

        return root;
    }

    private void getAllCharacterData(int page) {
        int limit = 15;

        Call<CharacterResponse> call = apiService.getAllCharacter(page, limit);
        call.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(@NonNull Call<CharacterResponse> call, @NonNull Response<CharacterResponse> response) {
                if(response.body() != null){
                    int oldCount = allCharacterResults.size();
                    binding.loadingAllCharacter.setVisibility(View.GONE);
                    binding.rvAllCharacterList.setVisibility(View.VISIBLE);

                    allCharacterResults.addAll(response.body().getCharacterResults());
                    allCharacterAdapter.notifyItemRangeInserted(oldCount, allCharacterResults.size());
                } else {
                    binding.loadingAllCharacter.setVisibility(View.GONE);
                    binding.textNoAllResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponse> call, @NonNull Throwable t) {
                binding.loadingAllCharacter.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to Fetch Data !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}