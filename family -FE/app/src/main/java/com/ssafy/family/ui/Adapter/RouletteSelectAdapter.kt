package com.ssafy.family.ui.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.RouletteFamilyListBinding

@RequiresApi(Build.VERSION_CODES.O)
class RouletteSelectAdapter (private val context: Context) : RecyclerView.Adapter<RouletteSelectAdapter.ViewHolder>() {

    var datas = listOf<MemberInfo>()
    var selected = hashMapOf<Long, Boolean>()

    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(val binding: RouletteFamilyListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item:MemberInfo){
            if(selected[item.profileId] == false){
                binding.rouletteFamilyImg.setImageResource(R.drawable.ic_baseline_close)
            }else{
                binding.rouletteFamilyImg.setImageResource(R.drawable.ic_baseline_check)
            }
            binding.rouletteFamilyText.text = item.nickname

            itemView.setOnClickListener {
                itemClickListener.onClick(item)
            }
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

    interface ItemClickListener {
        fun onClick(item: MemberInfo)
    }

}