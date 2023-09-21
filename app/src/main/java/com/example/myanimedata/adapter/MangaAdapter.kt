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
import com.example.myanimedata.adapter.MangaAdapter.MangaViewHolder
import com.example.myanimedata.api.MangaResult
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MangaAdapter(private val mangaResults: List<MangaResult>, private val context: Context) :
    RecyclerView.Adapter<MangaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        return MangaViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.container_manga, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        holder.bindItem(mangaResults[position], context)
    }

    override fun getItemCount(): Int {
        return mangaResults.size
    }

    class MangaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val loadingMangaItem: ProgressBar
        private val imageMangaPoster: RoundedImageView
        private val textMangaName: TextView

        init {
            loadingMangaItem = itemView.findViewById(R.id.loadingMangaItem)
            imageMangaPoster = itemView.findViewById(R.id.imageMangaPoster)
            textMangaName = itemView.findViewById(R.id.textMangaName)
        }

        fun bindItem(mangaResult: MangaResult, context: Context) {
            loadingMangaItem.visibility = View.GONE
            val imgUrl = Uri.parse(mangaResult.imageResults.jpgResults.imageUrl)
            Picasso.get().load(imgUrl).noFade().into(imageMangaPoster, object : Callback {
                override fun onSuccess() {
                    imageMangaPoster.animate().setDuration(500).alpha(1f).start()
                }

                override fun onError(e: Exception) {
                    Toast.makeText(
                        itemView.context, e.message + " cause : "
                                + e.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
            textMangaName.text = mangaResult.title
            itemView.setOnClickListener {
                val i = Intent(context, DetailActivity::class.java)
                i.putExtra("type", "manga")
                i.putExtra("manga_id", mangaResult.id)
                i.putExtra("manga_title", mangaResult.title)
                context.startActivity(i)
            }
        }
    }
}