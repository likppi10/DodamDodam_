package com.ssafy.family.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.remote.res.ScheduleInfo
import com.ssafy.family.databinding.ItemTodayScheduleBinding

// mainactivity - eventfragment : 오늘의 일정
class TodayScheduleAdapter(val onClick: (ScheduleInfo) -> Unit) : RecyclerView.Adapter<TodayScheduleAdapter.ViewHolder>() {

    var scheduleList = mutableListOf<ScheduleInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTodayScheduleBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int = scheduleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    inner class ViewHolder(val binding: ItemTodayScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onClick(scheduleList[bindingAdapterPosition])
            }
        }

        fun bind(scheduleInfo: ScheduleInfo) {
            binding.scheduleTextView.text = "${scheduleInfo.role} / ${scheduleInfo.title}"
        }
    }

}