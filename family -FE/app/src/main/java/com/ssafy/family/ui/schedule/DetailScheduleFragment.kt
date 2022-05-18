package com.ssafy.family.ui.schedule

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentDetailScheduleBinding
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.util.CalendarUtil.stringToLocalDate
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

private const val ScheduleId = "sId"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class DetailScheduleFragment : Fragment() {

    private lateinit var binding: FragmentDetailScheduleBinding
    private val detailScheduleViewModel by activityViewModels<DetailScheduleViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var scheduleId: Long? = null
    private val headerDateFormatter = DateTimeFormatter.ofPattern("MMM d'일'")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            scheduleId = it.getLong(ScheduleId)
            if(scheduleId == null){
                Toast.makeText(requireActivity(), "존재하지 않는 일정이에요.", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //헤더 및 parent 텍스트 초기화
        (activity as ScheduleActivity).apply {
            changeHeader("일정 상세","수정", "삭제")
            binding.scheduleButtonInclude.button.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.schedule_frame, EditScheduleFragment.newInstance(scheduleId!!)).commit()
            }
            binding.scheduleButtonInclude.button2.setOnClickListener {
                detailScheduleViewModel.deleteSchedule(scheduleId!!)
            }
            binding.scheduleTopInclude.backbtn.setOnClickListener {
                requireActivity().finish()
            }
        }

        //단위 일정 요청 및 결과 옵저버
        detailScheduleViewModel.getOneSchedule(scheduleId!!)
        detailScheduleViewModel.getOneRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(it.data!!.schedule != null){
                        binding.startdate.text = headerDateFormatter.format(stringToLocalDate(it.data.schedule!!.startDate))
                        binding.enddate.text = headerDateFormatter.format(stringToLocalDate(it.data.schedule.endDate))
                        binding.scheduleTitle.text = it.data.schedule.title
                        binding.scheduleContent.setText(it.data.schedule.content)
                    }
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "존재하지 않는 일정이에요.", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    requireActivity().finish()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //일정 삭제 요청 및 결과 옵저버
        detailScheduleViewModel.deleteRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "일정이 삭제되었어요.", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    requireActivity().finish()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //일정 로딩바
    private fun setLoading() {
        binding.progressBarDetailSLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarDetailSLoading.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(scheduleId: Long) =
            DetailScheduleFragment().apply {
                arguments = Bundle().apply {
                    putLong(ScheduleId, scheduleId)
                }
            }
    }

}