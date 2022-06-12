package com.ssafy.family.ui.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R

class StatusEmojiAdapter(private val context: Context): RecyclerView.Adapter<StatusEmojiAdapter.ViewHolder>() {

    var datas = mutableListOf<String>()
    var checkSelected = mutableListOf<Boolean>()
    var emojiSelected: String? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item:String){
            val imageView = itemView.findViewById<ImageView>(R.id.status_emoji_img)
            Glide.with(itemView).load(item).into(imageView)
            itemView.setOnClickListener {
                // 선택값 초기화
                checkSelected.forEachIndexed { index, b -> checkSelected[index] = false }
                // 값 선택
                checkSelected[absoluteAdapterPosition] = true
                emojiSelected = item
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.status_emoji_list, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 뷰홀더에 데이터 입력
        holder.bind(datas[position])

        // 선택여부 확인 후 백그라운드 지정
        if (checkSelected[position] == true) {
            holder.itemView.setBackgroundResource(R.drawable.emoji_select_box)
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }
    }

}