package com.ssafy.family.ui.main.bottomFragment

import androidx.lifecycle.*
import com.google.firebase.database.*
import com.ssafy.family.data.remote.res.ChatData
import com.ssafy.family.data.remote.res.ChattingRes
import com.ssafy.family.data.repository.ChatRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    var datas = mutableListOf<ChatData>()
    var database = FirebaseDatabase.getInstance()

    private val _getMemberRequestLiveData = MutableLiveData<Resource<ChattingRes>>()
    val getMemberRequestLiveData: LiveData<Resource<ChattingRes>>
        get() = _getMemberRequestLiveData

    fun send(data: ChatData, familyCode: String) {
        var myRef = database.getReference("message_" + familyCode)
        chatRepository.send(data, myRef)
        viewModelScope.launch {chatRepository.sendChattingFCM(data.message.toString())}
    }

    fun getMember() = viewModelScope.launch {
        _getMemberRequestLiveData.postValue(Resource.loading(null))
        _getMemberRequestLiveData.postValue(chatRepository.getMember())
    }

}