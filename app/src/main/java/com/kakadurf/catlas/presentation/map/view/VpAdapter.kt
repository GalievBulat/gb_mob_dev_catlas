package com.kakadurf.catlas.presentation.map.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kakadurf.catlas.R
import com.squareup.picasso.Picasso

class VpAdapter(val context: Context, val imagesUris: Array<String>) :
    RecyclerView.Adapter<ImageWrapper>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageWrapper =
        ImageWrapper(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fr_image_wrapper, parent, false)
        )

    override fun onBindViewHolder(holder: ImageWrapper, position: Int) {
        Picasso.with(context).load(imagesUris[position]).into(
            holder.itemView.findViewById(R.id.iv_context_image_main) as ImageView
        )
    }

    override fun getItemCount(): Int = imagesUris.size
}

class ImageWrapper(itemView: View) : RecyclerView.ViewHolder(itemView)
