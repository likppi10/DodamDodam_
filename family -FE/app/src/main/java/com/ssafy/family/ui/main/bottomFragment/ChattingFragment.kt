package com.ssafy.family.ui.main.bottomFragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.data.remote.res.ChatData
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.FragmentChattingBinding
import com.ssafy.family.ui.Adapter.ChattingAdapter
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.LoginUtil.getUserInfo
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding
    private val viewModel by activityViewModels<ChatViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    lateinit var chattingAdapter:ChattingAdapter
    val familyCode = getUserInfo()!!.familyId.toString()
    var myProfile = ""
    var memberList = listOf<MemberInfo>()
    var database = FirebaseDatabase.getInstance()
    var myRef = database!!.getReference("message_" + familyCode)
    var datas = mutableListOf<ChatData>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ApplicationClass.isChatting.postValue(true)
        Log.d("dddd", "onAttach: "+ ApplicationClass.isChatting.value)
    }

    override fun onDetach() {
        super.onDetach()
        ApplicationClass.isChatting.postValue(false)
        Log.d("dddd", "onDetach: "+ ApplicationClass.isChatting.value)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){

        viewModel.getMember()
        viewModel.getMemberRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    memberList = it.data!!.memberList
                    for(a in it.data.memberList){
                        if(a.profileId == getUserInfo()!!.profileId){
                            myProfile = a.profileImage
                            break
                        }
                    }
                    chattingAdapter = ChattingAdapter(memberList, mutableListOf())
                    setChatListener()
                    dismissLoading()
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
                    loginViewModel.MakeRefresh(getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.submitButton.setOnClickListener {
            val message: String = binding.chattingText.text.toString()
            if (message != "") {
                val now: LocalDateTime = LocalDateTime.now()
                val nowTime = now.format(
                    DateTimeFormatter.ofPattern("a h시 mm분").withLocale(Locale.forLanguageTag("ko"))
                )
                val data = ChatData(getUserInfo()!!.profileId, getUserInfo()!!.name, message, myProfile, nowTime)
                viewModel.send(data, familyCode)
                binding.chattingText.setText("")
            }
        }
    }

    private fun setChatListener() {
        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val data = snapshot.getValue(ChatData::class.java)
                datas.add(data!!)
                binding.chattingRecyclerView.adapter = chattingAdapter
                binding.chattingRecyclerView.scrollToPosition(chattingAdapter.itemCount-1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        myRef.addChildEventListener(childEventListener)
        chattingAdapter.datas = datas
        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true
        binding.chattingRecyclerView.layoutManager = manager
        binding.chattingRecyclerView.adapter = chattingAdapter
    }

    //로딩바
    private fun setLoading() {
        binding.progressBarLoading.visibility = View.VISIBLE
    }
    private fun dismissLoading() {
        binding.progressBarLoading.visibility = View.GONE
    }
}