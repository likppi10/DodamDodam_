package com.ssafy.family.ui.Adapter

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AlbumReaction
import com.ssafy.family.databinding.DetailAlbumCommentListBinding
import com.ssafy.family.util.LoginUtil

class DetailAlbumCommentAdapter(private val context: Context) :
    RecyclerView.Adapter<DetailAlbumCommentAdapter.ViewHolder>() {

    var datas = mutableListOf<AlbumReaction>()
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(val binding: DetailAlbumCommentListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item : AlbumReaction) {
            Glide.with(itemView).load(item.imagePath).into(binding.commentFamilyImg)
            Glide.with(itemView).load(item.emoticon).into(binding.commentFamilyEmoji)
            binding.commentFamilyRole.text=item.role
            Log.d("dddd", ".: "+item.profileId)
            Log.d("dddd", "itemitem: "+item)
            Log.d("dddd", "LoginUtil.getUserInfo()!!.profileId.toInt(): "+LoginUtil.getUserInfo()!!.profileId.toInt())
            if(item.profileId!=LoginUtil.getUserInfo()!!.profileId.toInt()){
                binding.commentFamilyDelete.visibility= View.GONE
            }
            binding.commentFamilyDelete.setOnClickListener { itemClickListener.onClick(item.reactionId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DetailAlbumCommentListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    interface ItemClickListener {
        fun onClick(reactionId:Int)
    }
}