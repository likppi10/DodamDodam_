package com.ssafy.family.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.family.data.remote.req.SendPushReq
import com.ssafy.family.data.remote.res.FamilyProfile
import com.ssafy.family.databinding.FragmentFamilyBinding
import com.ssafy.family.ui.Adapter.AlarmAdapter
import com.ssafy.family.ui.Adapter.StatusAdapter
import com.ssafy.family.ui.roulette.RouletteSelectDialog
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FamilyFragment : Fragment() {

    private lateinit var binding: FragmentFamilyBinding
    private lateinit var statusAdapter: StatusAdapter
    private val mainFamilyViewModel by activityViewModels<MainFamilyViewModel>()

    private val alarmClickListener = object :AlarmAdapter.ItemClickListener{

        override fun onClick(item: String, familyProfile: FamilyProfile) {
           mainFamilyViewModel.sendAlarm(SendPushReq(familyProfile.profileId.toString(),item))
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {

//        showSelectDialog("main")

        if(requireActivity().intent.getStringExtra("to") == "first"){
            showSelectDialog()
        }

        statusAdapter = StatusAdapter(requireActivity()).apply {
            itemClickListener=this@FamilyFragment.alarmClickListener
        }
        binding.statusRecyclerView.adapter = statusAdapter

        mainFamilyViewModel.getTodayMission()
        mainFamilyViewModel.getFamilyProfiles()

        mainFamilyViewModel.sendAlarmRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                }
            }
        }
        mainFamilyViewModel.todayMissionRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.layoutTodayExpression.boxtextTextView.text = it.data!!.dataSet!!.missionContent
                    dismissLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                }
            }
        }
        mainFamilyViewModel.familyProfileRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    statusAdapter.datas = it.data!!.dataSet as MutableList<FamilyProfile>
                    statusAdapter.notifyDataSetChanged()
                    dismissLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
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

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }
}