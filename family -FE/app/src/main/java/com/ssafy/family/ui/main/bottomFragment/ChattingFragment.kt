package com.ssafy.family.ui.main.bottomFragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.ssafy.family.config.ApplicationClass.Companion.isChatting
import com.ssafy.family.data.remote.res.ChatData
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.FragmentChattingBinding
import com.ssafy.family.ui.Adapter.ChattingAdapter
import com.ssafy.family.ui.home.LoginViewModel
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
    var check = false

    override fun onStart() {
        super.onStart()
        isChatting = MutableLiveData(true)
    }

    override fun onPause() {
        isChatting= MutableLiveData(false)
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMember()
        initView()
    }

    private fun initView(){

        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true

        binding.chattingRecyclerView.layoutManager = manager
        chattingAdapter = ChattingAdapter(listOf(), mutableListOf())

        binding.chattingRecyclerView.adapter = chattingAdapter
        chattingAdapter.datas = datas

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
                    chattingAdapter.memberList = memberList
                    if(!check){
                        setChatListener()
                    }
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
            if (message.length>500) {
                Toast.makeText(requireActivity(), "너무 보내고 싶은 말이 길어요 (500자 미만)", Toast.LENGTH_SHORT).show()
            }else if(message != "") {
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

    fun setChatListener() {
        check = true
        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val data = snapshot.getValue(ChatData::class.java)
                datas.add(data!!)
                binding.chattingRecyclerView.adapter!!.notifyDataSetChanged()
                binding.chattingRecyclerView.scrollToPosition(chattingAdapter.itemCount-1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        myRef.addChildEventListener(childEventListener)
    }

    //로딩바
    private fun setLoading() {
        binding.progressBarLoading.visibility = View.VISIBLE
    }
    private fun dismissLoading() {
        binding.progressBarLoading.visibility = View.GONE
    }

}