package com.ssafy.family.ui.wishtree

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.databinding.WishTreeDialogBinding

@RequiresApi(Build.VERSION_CODES.O)
class WIshBoxSelectDialog(context: Context, val profileImg: String?, val role: String, val content: String): Dialog(context) {

    private lateinit var binding: WishTreeDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WishTreeDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // profileImg
        val imageView = binding.wishTreeDialogProfileImage
        if (profileImg == null) {
            imageView.setImageResource(R.drawable.image_fail)
        } else {
            Glide.with(imageView).load(profileImg).into(imageView)
        }

        // text
        binding.wishTreeDialogRoleText.text = role
        binding.wishTreeDialogContentText.text = "\" $content \""

        // 확인 버튼 클릭시 다이얼로그 종료
        binding.wishTreeDialogConfirmBtn.setOnClickListener {
            dismiss()
        }
    }

}