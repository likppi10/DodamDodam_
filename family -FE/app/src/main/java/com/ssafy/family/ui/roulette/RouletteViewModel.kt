package com.ssafy.family.ui.roulette

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

class RouletteViewModel @AssistedInject constructor(
    private val chatRepository: ChatRepository
): ViewModel() {

    private val _getMemberRequestLiveData = MutableLiveData<Resource<ChattingRes>>()
    val getMemberRequestLiveData: LiveData<Resource<ChattingRes>>
        get() = _getMemberRequestLiveData

    fun getMember() = viewModelScope.launch {
        _getMemberRequestLiveData.postValue(Resource.loading(null))
        _getMemberRequestLiveData.postValue(chatRepository.getMember())
    }
}