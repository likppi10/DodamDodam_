package com.ssafy.family.ui.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.Alarm
import com.ssafy.family.data.remote.res.FamilyProfile
import com.ssafy.family.databinding.ItemFamilyStatusBinding

// mainactivity - familyfragment : 우리가족(상태)
class StatusAdapter(private val context: Context) :
    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {
    lateinit var itemClickListener: AlarmAdapter.ItemClickListener
    var datas = mutableListOf<FamilyProfile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFamilyStatusBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(datas[position])
//        val currentBook = getItemId(position)
        holder.bind((datas[position]))
    }

    inner class ViewHolder(val binding: ItemFamilyStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FamilyProfile) {
            Glide.with(itemView).load(item.imagePath).into(binding.familyProfileImage)

            if (item.emotion.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("https://s3-dodamdodam.s3.ap-northeast-2.amazonaws.com/emoticon/crying_1651797037512.png")
                    .into(binding.familyStatusImage)
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
                    Log.d("log닷", "onBindViewHolder: 1")
                    binding.alarmRecycler.visibility = GONE
                    binding.familyMessageButton.visibility = VISIBLE
                } else {
                    Log.d("log닷", "onBindViewHolder: 2")

                    binding.alarmRecycler.visibility = VISIBLE
                    binding.familyMessageButton.visibility = GONE
                    val adapter = AlarmAdapter(context).apply {
                        itemClickListener = this@StatusAdapter.itemClickListener
                    }
                    val gridLayoutManager =
                        GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
                    binding.alarmRecycler.layoutManager = gridLayoutManager
                    binding.alarmRecycler.adapter = adapter
                    val AlarmList = mutableListOf<Alarm>()
                    context.resources.getStringArray(R.array.expression).forEach {
                        AlarmList.add(Alarm(item, it))
                    }
                    adapter.datas = AlarmList
                    adapter.notifyDataSetChanged()
                }
            }
            binding.executePendingBindings()
        }
    }

}