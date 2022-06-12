package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.remote.res.HashTag
import com.ssafy.family.databinding.AddAlbumTagListBinding

class AlbumTagAdapter(private val context: Context) : RecyclerView.Adapter<AlbumTagAdapter.ViewHolder>()  {

    var datas = mutableListOf<HashTag>()
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(val binding: AddAlbumTagListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:HashTag){
            binding.addAlbumTagButton.text = item.text
            binding.addAlbumTagButton.setOnClickListener {
                itemClickListener.onClick(item)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AddAlbumTagListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    interface ItemClickListener {
        fun onClick(item: HashTag)
    }

}