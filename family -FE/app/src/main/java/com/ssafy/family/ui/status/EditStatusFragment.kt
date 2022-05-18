package com.ssafy.family.ui.status

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentEditStatusBinding
import com.ssafy.family.ui.Adapter.StatusEmojiAdapter
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class EditStatusFragment : Fragment() {

    private lateinit var binding: FragmentEditStatusBinding
    private lateinit var emojiAdapter: StatusEmojiAdapter
    private val statusViewModel by activityViewModels<StatusViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        statusViewModel.getFamilyPicture()
        statusViewModel.getMyStatus()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 가족사진 조회
        statusViewModel.selectedImgUri.observe(requireActivity()) {
            val imageView = binding.editStatusFamilyImage
            if (it == null){
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.image_fail))
            } else {
                Glide.with(imageView).load(it).into(imageView)
            }
        }

        // 이모지 어댑터 설정
        emojiAdapter = StatusEmojiAdapter(requireActivity())
        binding.statusEmojiRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = emojiAdapter
        }
        emojiAdapter.datas = mutableListOf()

        val emojis = resources.getStringArray(R.array.emoticon)

        // 오늘의 한마디 초기값 입력, 이모지 세팅
        statusViewModel.getMyStatus.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    emojiAdapter.datas.clear()
                    emojiAdapter.checkSelected.clear()
                    if (it.data?.data!!.comment == null && it.data?.data!!.emotion == null){
                        for (i in emojis.indices){
                            emojiAdapter.datas.add(emojis[i])
                            emojiAdapter.checkSelected.add(false)
                        }
                        emojiAdapter.notifyDataSetChanged()
                        binding.editStatusInputTodaysMessage.text = Editable.Factory.getInstance().newEditable(resources.getString(R.string.default_status_message))
                    } else {
                        for (i in emojis.indices){
                            if(emojis[i] == it.data.data.emotion){
                                emojiAdapter.datas.add(emojis[i])
                                emojiAdapter.checkSelected.add(true)
                                emojiAdapter.emojiSelected = it.data.data.emotion
                            }else{
                                emojiAdapter.datas.add(emojis[i])
                                emojiAdapter.checkSelected.add(false)
                            }
                        }
                        emojiAdapter.notifyDataSetChanged()
                        binding.editStatusInputTodaysMessage.text = Editable.Factory.getInstance().newEditable(it.data.data.comment)
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    statusViewModel.getMyStatus()
                }
            }
        }

        // 확인 버튼 클릭이벤트 리스너 등록
        binding.editStatusConfirmBtn.setOnClickListener {
            val emojiSelected = emojiAdapter.emojiSelected
            val todaysMessage = binding.editStatusInputTodaysMessage.text.toString()
            if (emojiSelected == null) {
                Toast.makeText(requireContext(), "오늘의 기분을 선택해주세요!", Toast.LENGTH_SHORT).show()
            } else if(todaysMessage.length>30 || todaysMessage.length<1){
                Toast.makeText(requireContext(), "오늘의 한마디는 30자 미만으로 입력해주세요!", Toast.LENGTH_SHORT).show()
            }else {
                statusViewModel.editMyStatus(emotion = emojiSelected, comment = todaysMessage)
            }
        }

        // 상태 변경 Res 확인
        statusViewModel.editStatusResponse.observe(requireActivity()) {
            if (it.status == Status.SUCCESS) {
                if(requireActivity().intent.getStringExtra("to") == "edit"){
                    Toast.makeText(requireContext(), "오늘의 상태 수정 완료!", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }else{
                    Toast.makeText(requireContext(), "오늘의 상태 입력 완료!", Toast.LENGTH_SHORT).show()
                    var intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("to", requireActivity().intent.getStringExtra("to"))
                    startActivity(intent)
                    requireActivity().finishAffinity()
                }
            }
        }
    }

}