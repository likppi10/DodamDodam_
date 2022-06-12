package com.ssafy.family.ui.startsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentAskFamilyCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskFamilyCodeFragment : Fragment() {
    private lateinit var binding: FragmentAskFamilyCodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAskFamilyCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 상단 텍스트 수정
        (activity as StartSettingActivity).changeTopMessage("가족 코드를 갖고 계신가요?")

        // 버튼별 클릭 이벤트 리스너 등록
        binding.askFamilyCodeAnswerYesBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_in_start_setting, WriteFamilyCodeFragment())
                .commit()
        }
        binding.askFamilyCodeAnswerNoBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_in_start_setting, SaveInfoFragment())
                .commit()
        }
    }

}