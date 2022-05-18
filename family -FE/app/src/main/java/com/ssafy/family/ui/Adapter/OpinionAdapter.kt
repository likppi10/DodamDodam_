package com.ssafy.family.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.remote.res.Opinion
import com.ssafy.family.databinding.ItemOpinionBinding

// mainactivity - familyfragment : 우리가족(상태)
class OpinionAdapter() : RecyclerView.Adapter<OpinionAdapter.ViewHolder>() {

    var opinionList = mutableListOf<Opinion>()
    lateinit var itemClickListener:ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOpinionBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int = opinionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(opinionList[position])
    }

    inner class ViewHolder(val binding: ItemOpinionBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.closeButton.close.setOnClickListener {
                itemClickListener.deleteClick(opinionList[bindingAdapterPosition])
            }
            binding.likeUnlike.likeIcon.setOnClickListener {
                itemClickListener.likeClick(opinionList[bindingAdapterPosition])
            }
            binding.likeUnlike.unlikeIcon.setOnClickListener {
                itemClickListener.unlikeClick(opinionList[bindingAdapterPosition])
            }
        }

        fun bind(opinion: Opinion) {
            binding.opinionText.setText(opinion.text)
            binding.likeUnlike.likeCnt.text = opinion.likeCount.toString()
            binding.likeUnlike.unlikeCnt.text = opinion.dislikeCount.toString()
        }
    }

    interface ItemClickListener {
        fun deleteClick(opinion: Opinion)
        fun likeClick(opinion: Opinion)
        fun unlikeClick(opinion: Opinion)
    }

}