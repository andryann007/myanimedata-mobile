package com.example.myanimedata.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.api.ImageResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder>{
    private final ArrayList<ImageResult> imageResults;

    public PictureAdapter(ArrayList<ImageResult> imageResults) {
        this.imageResults = imageResults;
    }

    @NonNull
    @Override
    public PictureAdapter.PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PictureViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_pictures, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PictureAdapter.PictureViewHolder holder, int position) {
        holder.bindItem(imageResults.get(position));
    }

    @Override
    public int getItemCount() {
        return imageResults.size();
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder{
        private final RoundedImageView imageView;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageList);
        }

        public void bindItem(ImageResult imageResult) {
            if(imageResult.getJpgResults() != null && imageResult.getWebpResults() != null){
                Uri imageUrl = Uri.parse(imageResult.getJpgResults().getImageUrl());
                Picasso.get().load(imageUrl).noFade().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.animate().setDuration(500).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(itemView.getContext(), e.getMessage() + " cause : "
                                + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if(imageResult.getJpgResults() != null){
                Uri imageUrl = Uri.parse(imageResult.getJpgResults().getImageUrl());
                Picasso.get().load(imageUrl).noFade().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.animate().setDuration(500).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(itemView.getContext(), e.getMessage() + " cause : "
                                + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if(imageResult.getWebpResults() != null){
                Uri imageUrl = Uri.parse(imageResult.getWebpResults().getImageUrl());
                Picasso.get().load(imageUrl).noFade().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.animate().setDuration(500).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(itemView.getContext(), e.getMessage() + " cause : "
                                + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                imageView.setImageResource(R.drawable.ic_no_image);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
    }
}
