package com.ssafy.family.ui.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.R

class PhotoPickAdapter(): RecyclerView.Adapter<PhotoPickAdapter.ViewHolder>()  {

    lateinit var itemClickListener: ItemClickListener
    var uris = arrayListOf<Uri>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.tag = false
        }
        fun bind(item: Uri) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView_albumF)
           imageView.setImageURI(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_pick_photo, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(uris[position])

    override fun getItemCount(): Int = uris.size

    interface ItemClickListener {
        fun onClick(uri: Uri, view: View, position: Int)
    }

}