package com.ssafy.family.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.data.repository.CalendarRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScheduleViewModel @Inject constructor(private val calendarRepository: CalendarRepository) : ViewModel() {

    private val _getOneRequestLiveData = MutableLiveData<Resource<ScheduleRes>>()
    val getOneRequestLiveData: LiveData<Resource<ScheduleRes>>
        get() = _getOneRequestLiveData

    private val _deleteRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val deleteRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _deleteRequestLiveData

    fun getOneSchedule(scheduleId: Long) = viewModelScope.launch {
        _getOneRequestLiveData.postValue(Resource.loading(null))
        _getOneRequestLiveData.postValue(calendarRepository.getSchedule(scheduleId))
    }

    fun deleteSchedule(scheduleId: Long) = viewModelScope.launch {
        _deleteRequestLiveData.postValue(Resource.loading(null))
        _deleteRequestLiveData.postValue(calendarRepository.deleteSchedule(scheduleId))
    }

}