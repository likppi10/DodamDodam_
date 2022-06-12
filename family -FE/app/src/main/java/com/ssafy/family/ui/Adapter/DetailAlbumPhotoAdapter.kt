package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AlbumPicture
import com.ssafy.family.databinding.AlbumPhotoListBinding

//detail: 0 ,add: 1, update :2
class DetailAlbumPhotoAdapter(private val context: Context, private val where: Int) : RecyclerView.Adapter<DetailAlbumPhotoAdapter.ViewHolder>() {

    var datas = mutableListOf<AlbumPicture>()
    lateinit var itemClickListener: ItemClickListener
    lateinit var deleteClickListener: DeleteClickListener

    inner class ViewHolder(val binding: AlbumPhotoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumPicture) {
            Glide.with(itemView).load(item.imagePath).into(binding.albumPhotoListImg)
            binding.albumPhotoListImg.setOnClickListener {
                itemClickListener.onClick(item)
            }
            if (where == 0) {
                itemView.background = null
                binding.button3.visibility = View.GONE
            } else if (where == 2) {
                if (item.main) {
                    itemView.background = ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.list_box_select,
                        null
                    )
                } else {
                    itemView.background = null
                }
                binding.button3.visibility= View.VISIBLE
                binding.button3.setOnClickListener {
                    deleteClickListener.onClick(item)
                }
            } else {
                binding.button3.visibility = View.GONE
                if (item.main) {
                    itemView.background = ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.list_box_select,
                        null
                    )
                } else {
                    itemView.background = null
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumPhotoListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    interface ItemClickListener {
        fun onClick(item: AlbumPicture)
    }

    interface DeleteClickListener {
        fun onClick(item: AlbumPicture)
    }

}
