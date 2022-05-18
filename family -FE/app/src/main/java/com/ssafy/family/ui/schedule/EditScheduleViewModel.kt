package com.ssafy.family.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.data.repository.CalendarRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditScheduleViewModel @Inject constructor(private val calendarRepository: CalendarRepository) : ViewModel() {

    private val _editRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val editRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _editRequestLiveData

    private val _getOneRequestLiveData = MutableLiveData<Resource<ScheduleRes>>()
    val getOneRequestLiveData: LiveData<Resource<ScheduleRes>>
        get() = _getOneRequestLiveData

    fun editSchedule(scheduleId: Long, scheduleReq: ScheduleReq) = viewModelScope.launch {
        _editRequestLiveData.postValue(Resource.loading(null))
        _editRequestLiveData.postValue(calendarRepository.editSchedule(scheduleId, scheduleReq))
    }

    fun getOneSchedule(scheduleId: Long) = viewModelScope.launch {
        _getOneRequestLiveData.postValue(Resource.loading(null))
        _getOneRequestLiveData.postValue(calendarRepository.getSchedule(scheduleId))
    }

}