package com.example.myanimedata.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.api.RecommendationResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>{
    private final ArrayList<RecommendationResult> recommendationResults;

    public RecommendationAdapter(ArrayList<RecommendationResult> recommendationResults) {
        this.recommendationResults = recommendationResults;
    }

    @NonNull
    @Override
    public RecommendationAdapter.RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_recommendations, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.RecommendationViewHolder holder, int position) {
        holder.bindItem(recommendationResults.get(position));
    }

    @Override
    public int getItemCount() {
        return recommendationResults.size();
    }

    public static class RecommendationViewHolder extends RecyclerView.ViewHolder{
        private final ProgressBar loadingRecommendationItem;
        private final RoundedImageView imgRecommendationItem;
        private final TextView textRecommendationItem;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);

            loadingRecommendationItem = itemView.findViewById(R.id.loadingRecommendationItem);
            imgRecommendationItem = itemView.findViewById(R.id.imageRecommendationPoster);
            textRecommendationItem = itemView.findViewById(R.id.textRecommendationList);
        }

        public void bindItem(RecommendationResult recommendationResult) {
            if(recommendationResult.getRecommendationResultDetail().getImageResult().getJpgResults() != null){
                loadingRecommendationItem.setVisibility(View.GONE);

                Uri recommendationImage = Uri.parse(recommendationResult.getRecommendationResultDetail().getImageResult().getJpgResults().getImageUrl());
                Picasso.get().load(recommendationImage).into(imgRecommendationItem);
            } else {
                loadingRecommendationItem.setVisibility(View.GONE);

                imgRecommendationItem.setImageResource(R.drawable.ic_no_image);
                imgRecommendationItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

            textRecommendationItem.setText(recommendationResult.getRecommendationResultDetail().getTitle());
        }
    }
}
