package com.example.myanimedata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanimedata.R;
import com.example.myanimedata.api.RoleResult;

import java.util.List;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder>{
    private final List<RoleResult> roleResults;

    public RoleAdapter(List<RoleResult> roleResults) {
        this.roleResults = roleResults;
    }

    @NonNull
    @Override
    public RoleAdapter.RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoleAdapter.RoleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_genre, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoleAdapter.RoleViewHolder holder, int position) {
        holder.bindItem(roleResults.get(position));
    }

    @Override
    public int getItemCount() {
        return roleResults.size();
    }

    public static class RoleViewHolder extends RecyclerView.ViewHolder{
        private final TextView textRole;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textRole = itemView.findViewById(R.id.textGenres);
        }

        public void bindItem(RoleResult roleResult) {
            textRole.setText(roleResult.getAnimeRoleResult().getTitle());
        }
    }
}
