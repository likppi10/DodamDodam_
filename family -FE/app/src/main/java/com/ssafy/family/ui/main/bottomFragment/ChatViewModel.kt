package com.ssafy.family.ui.main.bottomFragment

import androidx.lifecycle.*
import com.google.firebase.database.*
import com.ssafy.family.data.ChatData
import com.ssafy.family.data.remote.res.ChattingRes
import com.ssafy.family.data.repository.ChatRepository
import com.ssafy.family.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ChatViewModel @AssistedInject constructor(
    private val chatRepository: ChatRepository,
    @Assisted private var familyCode: String
): ViewModel() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database!!.getReference("message_" + familyCode)
    var datas = mutableListOf<ChatData>()

    private val _chatLiveData = MutableLiveData<Resource<MutableList<ChatData>>>()
    val chatLiveData : LiveData<Resource<MutableList<ChatData>>>
        get() = _chatLiveData

    private val _getMemberRequestLiveData = MutableLiveData<Resource<ChattingRes>>()
    val getMemberRequestLiveData: LiveData<Resource<ChattingRes>>
        get() = _getMemberRequestLiveData

    fun send(data: ChatData) = viewModelScope.launch {
        chatRepository.send(data, myRef)
    }
    fun initViewModel() = viewModelScope.launch {
        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val data = snapshot.getValue(ChatData::class.java)
                datas.add(data!!)
                _chatLiveData.postValue(Resource.success(datas))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        myRef.addChildEventListener(childEventListener)
    }

    fun getMember() = viewModelScope.launch {
        _getMemberRequestLiveData.postValue(Resource.loading(null))
        _getMemberRequestLiveData.postValue(chatRepository.getMember())
    }

    @AssistedFactory
    interface FamilyCodeAssistedFactory {
        fun create(familyCode: String): ChatViewModel
    }

    companion object {
        fun provideFactory(
            familyCodeAssistedFactory : FamilyCodeAssistedFactory,
            familyCode: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return familyCodeAssistedFactory.create(familyCode) as T
            }
        }
    }
}