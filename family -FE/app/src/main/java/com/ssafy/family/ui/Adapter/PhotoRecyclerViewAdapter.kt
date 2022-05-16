package com.ssafy.family.ui.Adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.MemberInfo

class PhotoRecyclerViewAdapter(): RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder>() {
    lateinit var itemClickListener: ItemClickListener
    var uris = mutableListOf<Uri>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.tag = false
        }
        fun bind(item: Uri, position: Int) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView_photoItem_clothes)
            Glide.with(imageView).load(item).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().into(imageView)
            itemView.tag = itemView.tag == false
            itemView.setOnClickListener{
                itemClickListener.onClick(item, itemView, position)
////                itemView.tag = itemView.tag == false
//                if(itemView.tag == true) {
//                    Log.d("ccccccccccc", "deClickdeClickdeClickdeClick: ")
////                    itemView.background = ResourcesCompat.getDrawable(itemView.resources, R.drawable.list_box_select, null)
//                } else {
//                    Log.d("ccccccccccc", "onClickonClickonClickonClick: ")
////                    itemView.background = null
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_photo_ltem, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        var iterData = uris.iterator()
//        while(iterData.hasNext()){
//            var result = iterData.next()
//            holder.bind(result.key)
//        }
        holder.bind(uris[position], position)
    }

    override fun getItemCount(): Int = uris.size

    interface ItemClickListener {
        fun onClick(uri: Uri, view: View, position: Int)
    }
}