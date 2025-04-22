package com.example.google_photo_sample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.google_photo_sample.R

class PhotoAdapter(private val photoList: List<String>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)

        init {
            itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(150).start()
                    v.elevation = 10f
                } else {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                    v.elevation = 0f
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUrl = photoList[position]
        Glide.with(holder.itemView.context)
            .load("$photoUrl=w400-h400")
            .into(holder.photoImageView)
    }

    override fun getItemCount(): Int = photoList.size
}
