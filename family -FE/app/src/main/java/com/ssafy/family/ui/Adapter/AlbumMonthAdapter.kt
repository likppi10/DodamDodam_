package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.databinding.ItemMonthAlbumBinding

// mainactivity - albumfragment : 월 리스트 (2021.06, 1,2,3 ...)
class AlbumMonthAdapter(private val context: Context) : RecyclerView.Adapter<AlbumMonthAdapter.ViewHolder>() {

    var datas = mutableListOf<AllAlbum>()
    lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMonthAlbumBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
        val itemBinding = holder.binding as ItemMonthAlbumBinding
    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        private val albumtag: TextView = itemView.findViewById(R.id.album_main_hashtag)
        private val albumimg: ImageView = itemView.findViewById(R.id.album_main_img)
        private val albumdate: TextView = itemView.findViewById(R.id.album_main_date)
        fun bind(item: AllAlbum) {
            albumdate.text = item.mainPicture.date
            Glide.with(itemView).load(item.mainPicture.imagePath).into(albumimg)
            var alltag = ""
            item.hashTags.forEach { alltag += it.text }
            albumtag.text = alltag
            itemView.setOnClickListener {
                itemClickListener.onClick(item)
            }
        }
    }

    interface ItemClickListener {
        fun onClick(allAlbum: AllAlbum)
    }

}