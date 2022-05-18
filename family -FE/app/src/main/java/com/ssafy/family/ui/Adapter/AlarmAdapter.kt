package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.remote.res.Alarm
import com.ssafy.family.data.remote.res.FamilyProfile
import com.ssafy.family.databinding.ItemAlarmBinding

// mainactivity - familyfragment : 알림 보내기 메시지 리스트
class AlarmAdapter(private val context: Context) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    var datas = mutableListOf<Alarm>()
    lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlarmBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Alarm) {
            binding.alarmText.text = item.text
            binding.rectangle1.setOnClickListener {
                itemClickListener.onClick(item.text,item.familyProfile)
            }
        }
    }

    interface ItemClickListener {
        fun onClick(item: String,familyProfile: FamilyProfile)
    }

}