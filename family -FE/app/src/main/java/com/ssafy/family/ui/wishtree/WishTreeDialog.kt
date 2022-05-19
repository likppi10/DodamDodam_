package com.ssafy.family.ui.wishtree

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.ssafy.family.databinding.WishTreeCreateDialogBinding
import com.ssafy.family.util.InputValidUtil

@RequiresApi(Build.VERSION_CODES.O)
class WishTreeDialog(context: Context, wishTreeDialogInterface: WishTreeDialogInterface, private val myWishListId: Int? = null, private val defaultContent: String? = null) : Dialog(context) {

    private lateinit var binding: WishTreeCreateDialogBinding
    private var wishTreeDialogInterface: WishTreeDialogInterface? = null

    init {
        this.wishTreeDialogInterface = wishTreeDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WishTreeCreateDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mode: String
        if (defaultContent == null) {
            mode = "create"
        } else {
            mode = "update"
        }

        if (mode == "create") {
            // 버튼 text 입력
            binding.wishTreeCreteDialogConfirmBtn.text = "소원 생성"
            // 클릭 리스너 등록
            binding.wishTreeCreteDialogConfirmBtn.setOnClickListener {
                if (isValidForm()) { // 유효성 검사
                    wishTreeDialogInterface?.onCreateBtnClicked(binding.wishTreeCreateDialogInputWish.text.toString())
                    dismiss()
                }
            }
        } else { // update
            // 버튼 text 입력
            binding.wishTreeCreteDialogConfirmBtn.text = "소원 수정"
            binding.wishTreeCreateDialogInputWish.text = Editable.Factory.getInstance().newEditable(defaultContent)
            // 클릭 리스너 등록
            binding.wishTreeCreteDialogConfirmBtn.setOnClickListener {
                if (isValidForm()) {
                    wishTreeDialogInterface?.onUpdateBtnClicked(myWishListId!!, binding.wishTreeCreateDialogInputWish.text.toString())
                    dismiss()
                }
            }
        }

        // 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // content 유효성 검사 계속 관찰
        binding.wishTreeCreateDialogInputWish.addTextChangedListener {
            val content = it.toString()
            if (InputValidUtil.isValidWish(content)) {
                dismissErrorOnWish()
            }
        }
    }

    // 데이터 유효성 검사
    private fun isValidForm(): Boolean {
        val content = binding.wishTreeCreateDialogInputWish.text.toString().trim()
        var flag = 1
        if (InputValidUtil.isValidWish(content)) {
            dismissErrorOnWish()
        } else {
            flag = 0
            setErrorOnWish(content)
        }
        return flag == 1
    }

    private fun setErrorOnWish(content: String) {
        if (content == "") {
            binding.textInputLayoutInputWish.error = "소원을 입력해 주세요!"
        } else {
            binding.textInputLayoutInputWish.error = "한글/영문/숫자 및 일부 특수문자(`~!?@#$%^&*()_=+)만 입력가능해요."
        }
    }

    private fun dismissErrorOnWish() {
        binding.textInputLayoutInputWish.error = null
    }

}