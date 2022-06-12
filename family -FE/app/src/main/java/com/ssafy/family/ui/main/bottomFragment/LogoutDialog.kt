package com.ssafy.family.ui.main.bottomFragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.ssafy.family.R
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class LogoutDialog(var context: Context) {

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.dialog_logout)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull( dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val cancle = dialog.findViewById<TextView>(R.id.cancle_dialog_text)
        val ok = dialog.findViewById<TextView>(R.id.ok_dialog_text)

        cancle.setOnClickListener {
            onClickListener.onClosed()
            dialog.dismiss()
        }

        ok.setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface OnDialogClickListener {
        fun onClicked()
        fun onClosed()
    }

}