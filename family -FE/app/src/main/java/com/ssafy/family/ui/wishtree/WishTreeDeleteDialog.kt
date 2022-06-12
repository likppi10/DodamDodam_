package com.ssafy.family.ui.wishtree

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.ssafy.family.databinding.DialogPermissionDeleteBinding

@RequiresApi(Build.VERSION_CODES.O)
class WishTreeDeleteDialog(context: Context, wishTreeDialogInterface: WishTreeDialogInterface, private val wishTreeId: Int, ) : Dialog(context){

    private lateinit var binding: DialogPermissionDeleteBinding
    private var wishTreeDialogInterface: WishTreeDialogInterface? = null

    init {
        this.wishTreeDialogInterface = wishTreeDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPermissionDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.askDeleteNegativeBtn.setOnClickListener {
            dismiss()
        }
        binding.askDeletePositiveBtn.setOnClickListener {
            wishTreeDialogInterface?.onDeleteBtnClicked(wishTreeId = wishTreeId)
            dismiss()
        }

        // 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}