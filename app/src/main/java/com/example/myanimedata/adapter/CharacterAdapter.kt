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
import com.example.myanimedata.adapter.CharacterAdapter.CharacterViewHolder
import com.example.myanimedata.api.CharacterResult
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharacterAdapter(
    private val CharacterResults: List<CharacterResult>,
    private val context: Context
) : RecyclerView.Adapter<CharacterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.container_character, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindItem(CharacterResults[position], context)
    }

    override fun getItemCount(): Int {
        return CharacterResults.size
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val loadingCharacterItem: ProgressBar
        private val imageCharacterPoster: RoundedImageView
        private val textCharacterName: TextView

        init {
            loadingCharacterItem = itemView.findViewById(R.id.loadingCharacterItem)
            imageCharacterPoster = itemView.findViewById(R.id.imageCharacterPoster)
            textCharacterName = itemView.findViewById(R.id.textCharacterName)
        }

        fun bindItem(characterResult: CharacterResult, context: Context) {
            loadingCharacterItem.visibility = View.GONE
            val imgUrl = Uri.parse(characterResult.imageResults.jpgResults.imageUrl)
            Picasso.get().load(imgUrl).noFade()
                .into(imageCharacterPoster, object : Callback {
                    override fun onSuccess() {
                        imageCharacterPoster.animate().setDuration(500).alpha(1f).start()
                    }

                    override fun onError(e: Exception) {
                        Toast.makeText(
                            itemView.context, e.message + " cause : "
                                    + e.cause, Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            textCharacterName.text = characterResult.name
            itemView.setOnClickListener {
                val i = Intent(context, DetailActivity::class.java)
                i.putExtra("type", "character")
                i.putExtra("id", characterResult.id)
                context.startActivity(i)
            }
        }
    }
}