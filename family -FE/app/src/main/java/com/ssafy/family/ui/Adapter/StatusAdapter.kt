package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.Alarm
import com.ssafy.family.data.remote.res.AlarmInfo
import com.ssafy.family.data.remote.res.FamilyProfile
import com.ssafy.family.databinding.ItemFamilyStatusBinding

// mainactivity - familyfragment : 우리가족(상태)
class StatusAdapter(private val context: Context) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    lateinit var itemClickListener: AlarmAdapter.ItemClickListener
    var datas = mutableListOf<FamilyProfile>()
    var alarmList = mutableListOf<AlarmInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFamilyStatusBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind((datas[position]))
    }

    inner class ViewHolder(val binding: ItemFamilyStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FamilyProfile) {
            if (item.imagePath.isNullOrEmpty()) {
                Glide.with(itemView).load(R.drawable.image_fail).into(binding.familyProfileImage)
            } else {
                Glide.with(itemView).load(item.imagePath).into(binding.familyProfileImage)
            }
            if (item.emotion.isNullOrEmpty()) {

            } else {
                Glide.with(itemView).load(item.emotion).into(binding.familyStatusImage)
            }

            if (item.comment.isNullOrEmpty()) {
                binding.familyStatus.text = context.getText(R.string.statusisnull)
            } else {
                binding.familyStatus.text = item.comment
            }
            binding.familyRole.text = item.role

            binding.statusLayout.setOnClickListener {
                if (binding.alarmRecycler.visibility == VISIBLE) {
                    binding.alarmRecycler.visibility = GONE
                } else {

                    binding.alarmRecycler.visibility = VISIBLE
                    val adapter = AlarmAdapter(context).apply {
                        itemClickListener = this@StatusAdapter.itemClickListener
                    }
                    val gridLayoutManager =
                        GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
                    binding.alarmRecycler.layoutManager = gridLayoutManager
                    binding.alarmRecycler.adapter = adapter
                    val AlarmList = mutableListOf<Alarm>()
                    alarmList.forEach {
                        AlarmList.add(Alarm(item, it.content))
                    }
                    adapter.datas = AlarmList
                    adapter.notifyDataSetChanged()
                }
            }
            binding.executePendingBindings()
        }
    }

}