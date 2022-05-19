package com.ssafy.family.ui.roulette

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.jhdroid.view.RotateListener
import com.ssafy.family.ui.Adapter.RouletteFamilyAdapter
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.ActivityRouletteBinding
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class RouletteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouletteBinding
    private val viewModel by viewModels<RouletteViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    var rouletteData = mutableListOf<String>()
    var memberFixList = mutableListOf<MemberInfo>()
    var memberSelectedList = mutableListOf<MemberInfo>()
    var fixCheck = hashMapOf<Long, Boolean>()
    var selectedCheck = hashMapOf<Long, Boolean>()
    var iterData = fixCheck.iterator()
    private lateinit var familyAdapter: RouletteFamilyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRouletteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getMember()
        viewModel.getMemberRequestLiveData.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    memberFixList = it.data!!.memberList.toMutableList()
                    memberSelectedList = it.data.memberList.toMutableList()
                    for(a in it.data.memberList){
                        fixCheck[a.profileId] = true
                        selectedCheck[a.profileId] = true
                        rouletteData.add(a.nickname)
                    }
                    dismissLoading()
                    if(memberFixList.size == 1){
                        Toast.makeText(this, "가족 인원이 2명이상이어야 이용이 가능해요.", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        initRoulette()
                    }

                }
                Status.ERROR -> {
                    Toast.makeText(this, "가족을 불러오지 못했어요.", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRoulette(){

        familyAdapter = RouletteFamilyAdapter(this)
        familyAdapter.datas = memberSelectedList

        binding.rouletteFamilyRecycler.apply {
            layoutManager = LinearLayoutManager(this@RouletteActivity, LinearLayoutManager.HORIZONTAL,false)
            adapter =  familyAdapter
        }

        binding.roulette.apply {
            setRouletteSize(rouletteData.size)
            setRouletteDataList(rouletteData)
        }

        binding.rouletteFamilyAdd.setOnClickListener {
            showSelectDialog()
        }

        binding.rouletteStart.setOnClickListener{
            rotateRoulette()
        }
    }

    private fun showSelectDialog(){

        var dialog = RouletteSelectDialog(this, selectedCheck, memberFixList)
        dialog.showDialog()

        dialog.setOnClickListener(object : RouletteSelectDialog.OnDialogClickListener {
            override fun onClicked(selectedList: HashMap<Long, Boolean>) {

                fixCheck.putAll(selectedList)

                rouletteData.clear()
                memberSelectedList.clear()

                iterData = fixCheck.iterator()
                while(iterData.hasNext()){
                    var result = iterData.next()
                    if(result.value){
                        var memberInfo: MemberInfo? = null
                        for(m in memberFixList){
                            if(m.profileId == result.key){
                                memberInfo = m
                            }
                        }
                        if (memberInfo != null) {
                            rouletteData.add(memberInfo.nickname)
                            memberSelectedList.add(memberInfo)
                        }
                    }
                }
                familyAdapter.notifyDataSetChanged()
                binding.roulette.apply {
                    setRouletteSize(rouletteData.size)
                    setRouletteDataList(rouletteData)
                }
            }

            override fun onClosed() {
                selectedCheck.putAll(fixCheck)
            }
        })
    }

    fun showResultDialog(memberInfo: MemberInfo){
        val dialog = RoulettetResultDialog(this, memberInfo)
        dialog.showDialog()
    }

    private fun rotateRoulette() {
        val rouletteListener = object : RotateListener {
            override fun onRotateStart() {
                // rotate animation start
            }

            override fun onRotateEnd(result: String) {
                var memberInfo: MemberInfo? = null
                for(m in memberFixList){
                    if(m.nickname == result){
                        memberInfo = m
                    }
                }
                showResultDialog(memberInfo!!)
            }
        }

        // random degrees (options)
        val toDegrees = (2000..10000).random().toFloat()
        binding.roulette.rotateRoulette(toDegrees, 4000, rouletteListener)
    }

    //월 단위 일정 로딩바
    private fun setLoading() {
        binding.progressBarLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoading.visibility = View.GONE
    }

}