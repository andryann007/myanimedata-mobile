package com.example.myanimedata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimedata.R
import com.example.myanimedata.adapter.GenreAdapter.GenreViewHolder
import com.example.myanimedata.api.GenreResult
import com.google.android.material.chip.Chip

class GenreAdapter(private val genreResults: List<GenreResult>) :
    RecyclerView.Adapter<GenreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.container_genre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bindItem(genreResults[position])
    }

    override fun getItemCount(): Int {
        return genreResults.size
    }

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textGenres: Chip

        init {
            textGenres = itemView.findViewById(R.id.textGenres)
        }

        fun bindItem(genreResult: GenreResult) {
            textGenres.text = genreResult.name
        }
    }
}