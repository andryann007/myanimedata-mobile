package com.example.myanimedata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimedata.R
import com.example.myanimedata.adapter.RoleAdapter.RoleViewHolder
import com.example.myanimedata.api.RoleResult
import com.google.android.material.chip.Chip

class RoleAdapter(private val roleResults: List<RoleResult>) :
    RecyclerView.Adapter<RoleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.container_genre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.bindItem(roleResults[position])
    }

    override fun getItemCount(): Int {
        return roleResults.size
    }

    class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textRole: Chip

        init {
            textRole = itemView.findViewById(R.id.textGenres)
        }

        fun bindItem(roleResult: RoleResult) {
            textRole.text = roleResult.animeRoleResult.title
        }
    }
}