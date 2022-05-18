package com.ssafy.family.ui.roulette

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.MemberInfo
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class RoulettetResultDialog(var context: Context, var memberInfo: MemberInfo) {

    private val winDialog = Dialog(context)

    fun showDialog() {

        winDialog.setContentView(R.layout.win_dialog)
        winDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull( winDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }
        winDialog.setCanceledOnTouchOutside(true)
        winDialog.setCancelable(true)
        winDialog.show()

        val win_dialog_img = winDialog.findViewById<ImageView>(R.id.win_dialog_img)
        val win_dialog_text = winDialog.findViewById<TextView>(R.id.win_dialog_text)

        if (memberInfo.profileImage.isNullOrEmpty()) {
            Glide.with(win_dialog_img).load(R.drawable.image_fail)
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .centerInside()
                .into(win_dialog_img)
        } else {
            Glide.with(win_dialog_img).load(memberInfo.profileImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .centerInside()
                .into(win_dialog_img)
        }

        win_dialog_text.text = memberInfo.nickname
    }

}