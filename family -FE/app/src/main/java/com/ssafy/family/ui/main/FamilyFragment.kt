package com.ssafy.family.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.family.data.remote.req.SendPushReq
import com.ssafy.family.data.remote.res.AlarmInfo
import com.ssafy.family.data.remote.res.FamilyProfile
import com.ssafy.family.databinding.FragmentFamilyBinding
import com.ssafy.family.ui.Adapter.AlarmAdapter
import com.ssafy.family.ui.Adapter.StatusAdapter
import com.ssafy.family.util.LoginUtil.getUserInfo
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FamilyFragment : Fragment() {

    private lateinit var binding: FragmentFamilyBinding
    private val mainFamilyViewModel by activityViewModels<MainFamilyViewModel>()
    private lateinit var statusAdapter: StatusAdapter

    private val alarmClickListener = object :AlarmAdapter.ItemClickListener{
        override fun onClick(item: String, familyProfile: FamilyProfile) {
           mainFamilyViewModel.sendAlarm(SendPushReq(familyProfile.profileId.toString(),item))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        if(requireActivity().intent.getStringExtra("to") == "first"){
            showSelectDialog()
        }

        statusAdapter = StatusAdapter(requireActivity()).apply {
            itemClickListener=this@FamilyFragment.alarmClickListener
        }
        binding.statusRecyclerView.adapter = statusAdapter

        mainFamilyViewModel.getTodayMission()
        mainFamilyViewModel.getFamilyProfiles()
        mainFamilyViewModel.getAlarmList(getUserInfo()!!.profileId.toInt())

        mainFamilyViewModel.sendAlarmRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading("sendAlarm", "success")
                }
                Status.ERROR -> {
                    dismissLoading("sendAlarm", "error")
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading("sendAlarm", "expired")
                }
            }
        }

        mainFamilyViewModel.todayMissionRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.layoutTodayExpression.boxtextTextView.text = it.data!!.dataSet!!.missionContent
                    dismissLoading("todayMission", "success")
                }
                Status.ERROR -> {
                    dismissLoading("todayMission", "error")
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading("todayMission", "expired")
                }
            }
        }

        mainFamilyViewModel.familyProfileRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    statusAdapter.datas = it.data!!.dataSet as MutableList<FamilyProfile>
                    statusAdapter.notifyDataSetChanged()
                    dismissLoading("familyProfile", "success")
                }
                Status.ERROR -> {
                    dismissLoading("familyProfile", "error")
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading("familyProfile", "expired")
                }
            }
        }

        mainFamilyViewModel.getAlarmListRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    statusAdapter.alarmList = it.data!!.dataSet as MutableList<AlarmInfo>
                    statusAdapter.notifyDataSetChanged()
                    dismissLoading("getAlarmList", "success")
                }
                Status.ERROR -> {
                    dismissLoading("getAlarmList", "error")
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading("getAlarmList", "expired")
                }
            }
        }
    }

    private fun showSelectDialog(){
        var dialog = GuideDialog(requireContext())
        dialog.showDialog()
    }

    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading(type: String, result: String) {
        if(binding.progressBarLoginFLoading.visibility == View.VISIBLE) {
            binding.progressBarLoginFLoading.visibility = View.GONE
            when (type) {
                "sendAlarm" -> {
                    if(result == "success"){
                        Toast.makeText(requireActivity(), "알람 전송에 성공했어요!", Toast.LENGTH_SHORT).show()
                    }else if(result == "error"){
                        Toast.makeText(requireActivity(), "알람 전송에 실패했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                "todayMission" -> {
                    if(result == "error"){
                        Toast.makeText(requireActivity(), "표현하기를 불러오지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                "familyProfile" -> {
                    if(result == "error"){
                        Toast.makeText(requireActivity(), "우리 가족을 불러오지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                "getAlarmList" -> {
                    if(result == "error"){
                        Toast.makeText(requireActivity(), "알람 리스트를 불러오지 못했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }else if(result == "expired"){
                        Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}