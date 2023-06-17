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
import com.example.myanimedata.api.MangaResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder>{
    private final List<MangaResult> mangaResults;
    private final Context context;

    public MangaAdapter(List<MangaResult> mangaResults, Context context) {
        this.mangaResults = mangaResults;
        this.context = context;
    }


    @NonNull
    @Override
    public MangaAdapter.MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MangaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_manga, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MangaAdapter.MangaViewHolder holder, int position) {
        holder.bindItem(mangaResults.get(position), context);
    }

    @Override
    public int getItemCount() {
        return mangaResults.size();
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder{
        private final RoundedImageView imageMangaPoster;
        private final TextView textMangaName;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMangaPoster = itemView.findViewById(R.id.imageMangaPoster);
            textMangaName = itemView.findViewById(R.id.textMangaName);
        }

        public void bindItem(MangaResult mangaResult, Context context) {

            if(mangaResult.getImageResults() != null){
                if(mangaResult.getImageResults().getJpgResults() != null){
                    Uri imgUrl = Uri.parse(mangaResult.getImageResults().getJpgResults().getImageUrl());
                    Picasso.get().load(imgUrl).into(imageMangaPoster);
                } else if (mangaResult.getImageResults().getWebpResults() != null){
                    Uri imgUrl = Uri.parse(mangaResult.getImageResults().getWebpResults().getImageUrl());
                    Picasso.get().load(imgUrl).into(imageMangaPoster);
                }
            } else {
                imageMangaPoster.setImageResource(R.drawable.ic_no_image_sm);
            }

            textMangaName.setText(mangaResult.getTitle());

            itemView.setOnClickListener(v ->{
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("type", "manga");
                i.putExtra("id", mangaResult.getId());
                context.startActivity(i);
            });
        }
    }
}
