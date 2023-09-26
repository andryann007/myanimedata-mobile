package com.example.myanimedata.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimedata.R
import com.example.myanimedata.adapter.PictureAdapter.PictureViewHolder
import com.example.myanimedata.api.ImageResult
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PictureAdapter(private val imageResults: ArrayList<ImageResult>) :
    RecyclerView.Adapter<PictureViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.container_pictures, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bindItem(imageResults[position])
    }

    override fun getItemCount(): Int {
        return imageResults.size
    }

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: RoundedImageView

        init {
            imageView = itemView.findViewById(R.id.imageList)
        }

        fun bindItem(imageResult: ImageResult) {
            val imageUrl = Uri.parse(imageResult.jpgResults?.imageUrl)
            Picasso.get().load(imageUrl).noFade().into(imageView, object : Callback {
                override fun onSuccess() {
                    imageView.animate().setDuration(500).alpha(1f).start()
                }

                override fun onError(e: Exception) {
                    Toast.makeText(
                        itemView.context, e.message + " cause : "
                                + e.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}