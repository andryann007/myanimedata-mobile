package com.example.myanimedata.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.activity.DetailActivity;
import com.example.myanimedata.api.CharacterResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>{
    private final List<CharacterResult> CharacterResults;
    private final Context context;

    public CharacterAdapter(List<CharacterResult> CharacterResults, Context context) {
        this.CharacterResults = CharacterResults;
        this.context = context;
    }

    @NonNull
    @Override
    public CharacterAdapter.CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_character, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.CharacterViewHolder holder, int position) {
        holder.bindItem(CharacterResults.get(position), context);
    }

    @Override
    public int getItemCount() {
        return CharacterResults.size();
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder{
        private final ProgressBar loadingCharacterItem;
        private final RoundedImageView imageCharacterPoster;
        private final TextView textCharacterName;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);

            loadingCharacterItem = itemView.findViewById(R.id.loadingCharacterItem);
            imageCharacterPoster = itemView.findViewById(R.id.imageCharacterPoster);
            textCharacterName = itemView.findViewById(R.id.textCharacterName);
        }

        public void bindItem(CharacterResult characterResult, Context context) {
            if(characterResult.getImageResults() != null){
                loadingCharacterItem.setVisibility(View.GONE);

                if(characterResult.getImageResults().getJpgResults() != null){
                    Uri imgUrl = Uri.parse(characterResult.getImageResults().getJpgResults().getImageUrl());
                    Picasso.get().load(imgUrl).into(imageCharacterPoster);
                } else if (characterResult.getImageResults().getWebpResults() != null){
                    Uri imgUrl = Uri.parse(characterResult.getImageResults().getWebpResults().getImageUrl());
                    Picasso.get().load(imgUrl).into(imageCharacterPoster);
                }
            } else {
                loadingCharacterItem.setVisibility(View.GONE);

                imageCharacterPoster.setImageResource(R.drawable.ic_no_image_sm);
            }

            textCharacterName.setText(characterResult.getName());

            itemView.setOnClickListener(v ->{
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("type", "character");
                i.putExtra("id", characterResult.getId());
                context.startActivity(i);
            });
        }
    }
}
