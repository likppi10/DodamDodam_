package com.ssafy.family.ui.roulette

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jhdroid.view.RotateListener
import com.ssafy.family.ui.Adapter.RouletteFamilyAdapter
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.ActivityRouletteBinding
import com.ssafy.family.ui.Adapter.ChattingAdapter
import com.ssafy.family.ui.main.bottomFragment.ChatViewModel
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class RouletteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouletteBinding
    private val viewModel by viewModels<ChatViewModel>()
    var rouletteData = mutableListOf<String>()
    var memberList = listOf<MemberInfo>()
    var check = hashMapOf<MemberInfo, Boolean>()
    var iterData = check.iterator()
    private lateinit var familyAdapter: RouletteFamilyAdapter
    lateinit var winDialog: Dialog
    lateinit var selectDialog: Dialog
    private fun setRecyclerViewClickListener(){
        familyAdapter.itemClickListener = object : RouletteFamilyAdapter.ItemClickListener{
            override fun onClick(item: MemberInfo) {
                if(familyAdapter.datas.size>2){
//                    rouletteData.remove(item.nickname)
                    check[item] = false
                    rouletteData.clear()

                    while(iterData.hasNext()){
                        var result = iterData.next()
                        if(result.value){
                            rouletteData.add(result.key.nickname)
                        }
                    }

                    binding.roulette.apply {
                        familyAdapter.notifyDataSetChanged()
                        setRouletteSize(rouletteData.size)
                        setRouletteDataList(rouletteData)
                    }
                }else{
                    Toast.makeText(this@RouletteActivity,"최소 2인 이상이여야합니다",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouletteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        winDialog = Dialog(this)
        winDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //모서리 둥글게하는거 뒷 배경처리 예외처리
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull( winDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }
        winDialog.setContentView(R.layout.win_dialog)
        winDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        familyAdapter = RouletteFamilyAdapter(this)

        viewModel.getMember()
        viewModel.getMemberRequestLiveData.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    memberList = it.data!!.memberList
                    for(a in it.data.memberList){
                        check[a] = true
                        rouletteData.add(a.nickname)
                    }
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message!!, Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }

        rouletteData.add("아빠")
        rouletteData.add("엄마")

//        familyAdapter.datas = rouletteData
        binding.rouletteFamilyRecycler.apply {
            layoutManager = LinearLayoutManager(this@RouletteActivity,
                LinearLayoutManager.HORIZONTAL,false)
            adapter =  familyAdapter
        }
        setRecyclerViewClickListener()

        binding.roulette.apply {
            setRouletteSize(rouletteData.size)
            setRouletteDataList(rouletteData)
        }
        binding.rouletteFamilyAdd.setOnClickListener {
            rouletteData.add("아들")
            familyAdapter.notifyDataSetChanged()
            binding.roulette.apply {
                setRouletteSize(rouletteData.size)
                setRouletteDataList(rouletteData)
            }
        }
        binding.rouletteStart.setOnClickListener{
            rotateRoulette()
        }
    }
    fun showDialog(){
        winDialog.show()
    }
    fun rotateRoulette() {
        val rouletteListener = object : RotateListener {
            override fun onRotateStart() {
                // rotate animation start
            }

            override fun onRotateEnd(result: String) {
                showDialog()
                //Toast.makeText(this@RouletteActivity,result,Toast.LENGTH_SHORT).show()
                // rotate animation end
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