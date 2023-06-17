package com.example.myanimedata.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.activity.DetailActivity;
import com.example.myanimedata.api.AnimeResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>{
    private final List<AnimeResult> animeResults;
    private final Context context;

    public AnimeAdapter(List<AnimeResult> animeResults, Context context) {
        this.animeResults = animeResults;
        this.context = context;
    }


    @NonNull
    @Override
    public AnimeAdapter.AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimeViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_anime, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeAdapter.AnimeViewHolder holder, int position) {
        holder.bindItem(animeResults.get(position), context);
    }

    @Override
    public int getItemCount() {
        return animeResults.size();
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder{
        private final RoundedImageView imageAnimePoster;
        private final TextView textAnimeName;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageAnimePoster = itemView.findViewById(R.id.imageAnimePoster);
            textAnimeName = itemView.findViewById(R.id.textAnimeName);
        }

        public void bindItem(AnimeResult animeResult, Context context) {

            if(animeResult.getImageResults() != null){
                if(animeResult.getImageResults().getJpgResults() != null){
                    Uri imgUrl = Uri.parse(animeResult.getImageResults().getJpgResults().getImageUrl());
                    Picasso.get().load(imgUrl).into(imageAnimePoster);
                } else if (animeResult.getImageResults().getWebpResults() != null){
                    Uri imgUrl = Uri.parse(animeResult.getImageResults().getWebpResults().getImageUrl());
                    Picasso.get().load(imgUrl).into(imageAnimePoster);
                }
            } else {
                imageAnimePoster.setImageResource(R.drawable.ic_no_image_sm);
            }

            textAnimeName.setText(animeResult.getTitle());

            itemView.setOnClickListener(v ->{
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("type", "anime");
                i.putExtra("id", animeResult.getId());
                context.startActivity(i);
            });
        }
    }
}
