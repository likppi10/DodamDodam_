package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.RouletteFamilyListBinding

class RouletteFamilyAdapter (private val context: Context) : RecyclerView.Adapter<RouletteFamilyAdapter.ViewHolder>() {

    var datas = mutableListOf<MemberInfo>()

    inner class ViewHolder(val binding: RouletteFamilyListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:MemberInfo){

            if(item.profileImage == null){
                Glide.with(binding.rouletteFamilyImg).load(R.drawable.image_fail)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .centerInside()
                    .into(binding.rouletteFamilyImg)
            }else{
                Glide.with(binding.rouletteFamilyImg).load(item.profileImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .centerInside()
                    .into(binding.rouletteFamilyImg)
            }

            binding.rouletteFamilyText.text = item.nickname

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RouletteFamilyListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

}