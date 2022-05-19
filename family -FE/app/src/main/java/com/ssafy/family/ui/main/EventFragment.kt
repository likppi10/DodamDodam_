package com.ssafy.family.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.remote.res.ScheduleInfo
import com.ssafy.family.data.remote.req.OpinionReactionReq
import com.ssafy.family.data.remote.res.Opinion
import com.ssafy.family.ui.Adapter.TodayScheduleAdapter
import com.ssafy.family.databinding.FragmentEventBinding
import com.ssafy.family.ui.Adapter.OpinionAdapter
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.roulette.RouletteActivity
import com.ssafy.family.ui.schedule.ScheduleActivity
import com.ssafy.family.ui.wishtree.WishTreeActivity
import com.ssafy.family.util.CalendarUtil
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding
    private val eventViewModel by activityViewModels<EventViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var eventsAdapter: TodayScheduleAdapter? = null
    private var opinionAdapter: OpinionAdapter? = null
    private var scheduleDayList = mutableListOf<ScheduleInfo>()
    private var opinionsList = mutableListOf<Opinion>()
    private val today = LocalDate.now()

    private val itemClickListener = object : OpinionAdapter.ItemClickListener {
        override fun deleteClick(opinion: Opinion) {
            eventViewModel.deleteOpinion(opinion.suggestionId)
        }
        override fun likeClick(opinion: Opinion) {
            eventViewModel.addOpinionReaction(OpinionReactionReq(opinion.suggestionId,true))
        }
        override fun unlikeClick(opinion: Opinion) {
            eventViewModel.addOpinionReaction(OpinionReactionReq(opinion.suggestionId,false))
        }
    }

    override fun onResume() {
        super.onResume()
        eventViewModel.getDaySchedule(CalendarUtil.dayLocalDateToString(today))
        eventViewModel.getOpinion()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        eventsAdapter = TodayScheduleAdapter {
            val intent = Intent(requireContext(),ScheduleActivity::class.java)
            intent.putExtra("sID", it.scheduleId)
            startActivity(intent)
        }
        binding.todayScheduleRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
        }

        opinionAdapter = OpinionAdapter().apply {
            itemClickListener = this@EventFragment.itemClickListener
        }
        binding.opinionRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = opinionAdapter
        }

        binding.addScheduleButton.setOnClickListener {
            val intent = Intent(requireContext(), ScheduleActivity::class.java)
            startActivity(intent)
        }

        binding.addOpinionButton.setOnClickListener {
            if(binding.opinionText.text.length<20){
                eventViewModel.addOpinion(binding.opinionText.text.toString())
            }else{
                Toast.makeText(requireActivity(), "의견은 20자 미만으로 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        eventViewModel.getDayRequestLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.schedules.isNullOrEmpty()){
                        scheduleDayList = it.data.schedules.toMutableList()
                        updateScheduleAdapter()
                    }else{
                        scheduleDayList = mutableListOf()
                        updateScheduleAdapter()
                    }
                    dismissScheduleLoading("getDay","success")
                }
                Status.ERROR -> {
                    dismissScheduleLoading("getDay","error")
                }
                Status.LOADING -> {
                    setScheduleLoading()
                }
                Status.EXPIRED -> {
                    dismissScheduleLoading("getDay","expired")
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                }
            }
        }

        eventViewModel.getOpinionRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.opinions.isNullOrEmpty()){
                        opinionsList = it.data.opinions.toMutableList()
                        updateOpinionAdapter()
                    }else{
                        opinionsList = mutableListOf()
                        updateOpinionAdapter()
                    }
                    dismissOpinionLoading("getOpinion","success")
                }
                Status.ERROR -> {
                    dismissOpinionLoading("getOpinion","error")
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading("getOpinion","expired")
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                }
            }
        }

        eventViewModel.addOpinionReactionRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.opinions.isNullOrEmpty()){
                        opinionsList = it.data.opinions.toMutableList()
                        updateOpinionAdapter()
                    }else{
                        opinionsList = mutableListOf()
                        updateOpinionAdapter()
                    }
                    dismissOpinionLoading("addOpinionReaction","success")
                }
                Status.ERROR -> {
                    dismissOpinionLoading("addOpinionReaction","error")
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading("addOpinionReaction","expired")
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                }
            }
        }

        eventViewModel.addOpinionRequestLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    dismissOpinionLoading("addOpinion","success")
                    eventViewModel.getOpinion()
                }
                Status.ERROR -> {
                    dismissOpinionLoading("addOpinion","error")
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading("addOpinion","expired")
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)

                }
            }
        }

        eventViewModel.deleteOpinionRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    dismissOpinionLoading("deleteOpinion","success")
                    eventViewModel.getOpinion()
                }
                Status.ERROR -> {
                    dismissOpinionLoading("deleteOpinion","error")
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading("deleteOpinion","expired")
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                }
            }
        }

        binding.rouletteIcon.setOnClickListener {
            val intent = Intent(requireContext(), RouletteActivity::class.java)
            startActivity(intent)
        }

        binding.wishtreeIcon.setOnClickListener {
            val intent = Intent(requireContext(), WishTreeActivity::class.java)
            startActivity(intent)
        }

        binding.surpriseIcon.setOnClickListener {
            Toast.makeText(requireActivity(), "서비스 준비 중이에요. ^^", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateScheduleAdapter() {
        eventsAdapter?.apply {
            scheduleList.clear()
            scheduleList.addAll(scheduleDayList)
            notifyDataSetChanged()
        }
    }

    private fun updateOpinionAdapter() {
        opinionAdapter?.apply {
            if(opinionsList.size >= 3){
                binding.addOpinionView.visibility = GONE
            }else{
                binding.addOpinionView.visibility = VISIBLE
            }
            binding.opinionText.text = null
            opinionList.clear()
            opinionList.addAll(opinionsList)
            notifyDataSetChanged()
        }
    }

    private fun setScheduleLoading() {
        binding.progressBarDayLoading.visibility = VISIBLE
    }

    private fun dismissScheduleLoading(type: String, result: String) {
        if(binding.progressBarOpinionLoading.visibility == VISIBLE) {
            binding.progressBarDayLoading.visibility = GONE
            if(type == "getDay"){
                if(result == "error"){
                    Toast.makeText(requireActivity(), "일정을 불러오지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }else if(result == "expired"){
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setOpinionLoading() {
        binding.progressBarOpinionLoading.visibility = VISIBLE
    }

    private fun dismissOpinionLoading(type: String, result: String) {
        if(binding.progressBarOpinionLoading.visibility == VISIBLE) {
            binding.progressBarOpinionLoading.visibility = GONE
            when (type) {
                "getOpinion" -> {
                    if(result == "error"){
                        Toast.makeText(requireActivity(), "의견들을 불러오지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                "addOpinionReaction" -> {
                    if(result == "error"){
                        Toast.makeText(requireActivity(), "리액션을 추가하지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                "addOpinion" -> {
                    if(result == "success"){
                        Toast.makeText(requireActivity(), "의견 등록이 완료되었어요", Toast.LENGTH_SHORT).show()
                    }else if(result == "error"){
                        Toast.makeText(requireActivity(), "의견을 등록하지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                "deleteOpinion" -> {
                    if(result == "success"){
                        Toast.makeText(requireActivity(), "의견 삭제가 완료되었어요", Toast.LENGTH_SHORT).show()
                    }else if(result == "error"){
                        Toast.makeText(requireActivity(), "의견을 삭제하지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}