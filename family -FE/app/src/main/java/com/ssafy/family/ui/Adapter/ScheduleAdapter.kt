package com.ssafy.family.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.remote.res.ScheduleInfo
import com.ssafy.family.databinding.ItemScheduleBinding

class ScheduleAdapter(val onClick: (ScheduleInfo) -> Unit) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    var scheduleList = mutableListOf<ScheduleInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ScheduleViewHolder(
            ItemScheduleBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ScheduleViewHolder, position: Int) {
        viewHolder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int = scheduleList.size

    inner class ScheduleViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(scheduleList[bindingAdapterPosition])
            }
        }

        fun bind(scheduleInfo: ScheduleInfo) {
            binding.itemScheduleTitle.text = scheduleInfo.title
            binding.itemScheduleRole.text = scheduleInfo.role
        }
    }

}
