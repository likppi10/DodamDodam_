package com.ssafy.family.ui.startsetting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentWriteFamilyCodeBinding
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.InputValidUtil
import com.ssafy.family.util.UiMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteFamilyCodeFragment : Fragment() {

    private lateinit var binding: FragmentWriteFamilyCodeBinding
    private val familyViewModel by activityViewModels<StartSettingViewModel>()

    lateinit var familyCode: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWriteFamilyCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 상단 텍스트 수정
        (activity as StartSettingActivity).changeTopMessage("가족 코드를 입력해주세요!")

        // 클릭 이벤트 등록
        binding.writeFamilyCodeMoveNextBtn.setOnClickListener{
            familyCode = binding.writeFamilyCodeInputText.text.toString()
            if (checkFamilyCode(familyCode)) {
                // 유효성 검사 통과하면 실행
                handleButtonUI(UiMode.PROGRESS)
                val code = familyCode.uppercase()
                // 요청 성공 시 뷰모델 checkFamilyCodeRes(familyId), isChecked(화면전환) 설정
                val res = familyViewModel.checkFamilyCode(code)
                Log.d(TAG, "WriteFamilyCodeFragment - onViewCreated() called $res")
            } else {
                // 유효성 검사 실패 시 로그확인
                Log.d(TAG, "WriteFamilyCodeFragment - onViewCreated() called $familyCode")
            }
        }

        // 뷰모델 데이터 변화 감지
        familyViewModel.isChecked.observe(requireActivity()){
            // 가족코드 검증 성공 시 화면 전환
            if (it == UiMode.READY) {
                parentFragmentManager.beginTransaction().replace(R.id.fragment_in_start_setting, SaveInfoFragment()).commit()
            } else if (it == UiMode.FAIL) {
                Toast.makeText(requireContext(), "가족 코드를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                handleButtonUI(UiMode.READY)
            }
        }

        // 유효성 검사
        binding.writeFamilyCodeInputText.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidFamilyCode(input)) {
                dismissErrorOnFamilyCode()
            } else {
                setErrorOnFamilyCode()
            }
        }
    } //onViewCreated

    // 프로그래스바 설정
    private fun handleButtonUI(mode: UiMode) {
        when (mode) {
            UiMode.PROGRESS -> {
                binding.writeFamilyCodeBtnProgress.visibility = View.VISIBLE
                binding.writeFamilyCodeMoveNextBtn.visibility = View.INVISIBLE
            }
            else -> {
                binding.writeFamilyCodeBtnProgress.visibility = View.INVISIBLE
                binding.writeFamilyCodeMoveNextBtn.visibility = View.VISIBLE
            }
        }
    }

    // 유효성 검사
    private fun checkFamilyCode(familyCode: String): Boolean {
        var flag = 1
        if (InputValidUtil.isValidFamilyCode(familyCode)) {
            dismissErrorOnFamilyCode()
        } else {
            flag = 0
            setErrorOnFamilyCode()
        }
        return flag == 1
    }

    private fun dismissErrorOnFamilyCode() {
        binding.writeFamilyCodeInputText.error = null
    }

    private fun setErrorOnFamilyCode() {
        binding.writeFamilyCodeInputText.error = resources.getString(R.string.familyCodeErrorMessage)
    }

}