package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.databinding.DetailAlbumEmojiListBinding

class DetailAlbumEmojiAdapter(private val context: Context) :RecyclerView.Adapter<DetailAlbumEmojiAdapter.ViewHolder>() {

    var datas = mutableListOf<String>()
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(val binding: DetailAlbumEmojiListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:String){
            Glide.with(itemView).load(item).into(binding.aetailAlbumEmojiImg)
            binding.aetailAlbumEmojiImg.setOnClickListener {
                itemClickListener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DetailAlbumEmojiListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    interface ItemClickListener {
        fun onClick(item:String)
    }

}