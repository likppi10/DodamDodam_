package com.ssafy.family.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.OpinionReactionReq
import com.ssafy.family.data.remote.req.OpinionReq
import com.ssafy.family.data.remote.res.OpinionRes
import com.ssafy.family.data.remote.res.SchedulesRes
import com.ssafy.family.data.repository.CalendarRepository
import com.ssafy.family.data.repository.MainEventRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val calendarRepository: CalendarRepository, private val mainEventRepository: MainEventRepository) : ViewModel() {

    private val _getDayRequestLiveData = MutableLiveData<Resource<SchedulesRes>>()
    val getDayRequestLiveData: LiveData<Resource<SchedulesRes>>
        get() = _getDayRequestLiveData

    private val _getOpinionRequestLiveData = MutableLiveData<Resource<OpinionRes>>()
    val getOpinionRequestLiveData: LiveData<Resource<OpinionRes>>
        get() = _getOpinionRequestLiveData

    private val _addOpinionReactionRequestLiveData = MutableLiveData<Resource<OpinionRes>>()
    val addOpinionReactionRequestLiveData: LiveData<Resource<OpinionRes>>
        get() = _addOpinionReactionRequestLiveData

    private val _addOpinionRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val addOpinionRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _addOpinionRequestLiveData

    private val _deleteOpinionRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val deleteOpinionRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _deleteOpinionRequestLiveData


    fun getDaySchedule(day: String) = viewModelScope.launch {
        _getDayRequestLiveData.postValue(Resource.loading(null))
        _getDayRequestLiveData.postValue(calendarRepository.getDaySchedule(day))
    }

    fun getOpinion() = viewModelScope.launch {
        _getOpinionRequestLiveData.postValue(Resource.loading(null))
        _getOpinionRequestLiveData.postValue(mainEventRepository.getOpinion())
    }

    fun addOpinionReaction(opinionReactionReq: OpinionReactionReq) = viewModelScope.launch {
        _addOpinionReactionRequestLiveData.postValue(Resource.loading(null))
        _addOpinionReactionRequestLiveData.postValue(mainEventRepository.addOpinionReaction(opinionReactionReq))
    }

    fun addOpinion(text: String) = viewModelScope.launch {
        _addOpinionRequestLiveData.postValue(Resource.loading(null))
        _addOpinionRequestLiveData.postValue(mainEventRepository.addOpinion(OpinionReq(text)))
    }

    fun deleteOpinion(suggestionId :Long) = viewModelScope.launch {
        _deleteOpinionRequestLiveData.postValue(Resource.loading(null))
        _deleteOpinionRequestLiveData.postValue(mainEventRepository.deleteOpinion(suggestionId))
    }

}