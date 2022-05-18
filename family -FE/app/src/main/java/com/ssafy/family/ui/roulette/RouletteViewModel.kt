package com.ssafy.family.ui.roulette

import androidx.lifecycle.*
import com.ssafy.family.data.remote.res.ChattingRes
import com.ssafy.family.data.repository.ChatRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouletteViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {

    private val _getMemberRequestLiveData = MutableLiveData<Resource<ChattingRes>>()
    val getMemberRequestLiveData: LiveData<Resource<ChattingRes>>
        get() = _getMemberRequestLiveData

    fun getMember() = viewModelScope.launch {
        _getMemberRequestLiveData.postValue(Resource.loading(null))
        _getMemberRequestLiveData.postValue(chatRepository.getMember())
    }

}