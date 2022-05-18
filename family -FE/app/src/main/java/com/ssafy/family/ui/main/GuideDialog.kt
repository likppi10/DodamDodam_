package com.ssafy.family.ui.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.ssafy.family.R
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class GuideDialog(var context: Context) {

    private val dialog = Dialog(context)

    fun showDialog() {

        dialog.setContentView(R.layout.dialog_guide)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull( dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }

        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }

}