package com.example.myanimedata.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimedata.R
import com.example.myanimedata.activity.DetailActivity
import com.example.myanimedata.adapter.AnimeAdapter.AnimeViewHolder
import com.example.myanimedata.api.AnimeResult
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class AnimeAdapter(private val animeResults: List<AnimeResult>, private val context: Context) :
    RecyclerView.Adapter<AnimeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        return AnimeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.container_anime, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bindItem(animeResults[position], context)
    }

    override fun getItemCount(): Int {
        return animeResults.size
    }

    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val loadingAnimeItem: ProgressBar
        private val imageAnimePoster: RoundedImageView
        private val textAnimeName: TextView

        init {
            loadingAnimeItem = itemView.findViewById(R.id.loadingAnimeItem)
            imageAnimePoster = itemView.findViewById(R.id.imageAnimePoster)
            textAnimeName = itemView.findViewById(R.id.textAnimeName)
        }

        fun bindItem(animeResult: AnimeResult, context: Context) {
            loadingAnimeItem.visibility = View.GONE
            val imgUrl = Uri.parse(animeResult.imageResults.jpgResults.imageUrl)
            Picasso.get().load(imgUrl).noFade().into(imageAnimePoster, object : Callback {
                override fun onSuccess() {
                    imageAnimePoster.animate().setDuration(500).alpha(1f).start()
                }

                override fun onError(e: Exception) {
                    Toast.makeText(
                        itemView.context, e.message + " cause : "
                                + e.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
            textAnimeName.text = animeResult.title
            itemView.setOnClickListener {
                val i = Intent(context, DetailActivity::class.java)
                i.putExtra("type", "anime")
                i.putExtra("id", animeResult.id)
                context.startActivity(i)
            }
        }
    }
}