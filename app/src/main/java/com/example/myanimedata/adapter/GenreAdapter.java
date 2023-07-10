package com.example.myanimedata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.api.GenreResult;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder>{
    private final List<GenreResult> genreResults;

    public GenreAdapter(List<GenreResult> genreResults) {
        this.genreResults = genreResults;
    }


    @NonNull
    @Override
    public GenreAdapter.GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreAdapter.GenreViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_genre, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.GenreViewHolder holder, int position) {
        holder.bindItem(genreResults.get(position));
    }

    @Override
    public int getItemCount() {
        return genreResults.size();
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder{
        private final TextView textGenres;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);

            textGenres = itemView.findViewById(R.id.textGenres);
        }

        public void bindItem(GenreResult genreResult){
            textGenres.setText(genreResult.getName());
        }
    }
}
