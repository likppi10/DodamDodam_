package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.RouletteFamilyListBinding

class RouletteFamilyAdapter (private val context: Context) : RecyclerView.Adapter<RouletteFamilyAdapter.ViewHolder>() {

    var datas = mutableListOf<MemberInfo>()
    lateinit var itemClickListener: ItemClickListener
    inner class ViewHolder(val binding: RouletteFamilyListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:MemberInfo){
            binding.rouletteFamilyImg.setImageResource(R.drawable.amusing)
            //binding.rouletteFamilyText.text = item
            itemView.setOnClickListener {
                itemClickListener.onClick(item)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_family_status,parent,false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = RouletteFamilyListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    interface ItemClickListener {
        fun onClick(item: MemberInfo)
    }
}