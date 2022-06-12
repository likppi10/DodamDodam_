package com.ssafy.family.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.repository.CalendarRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(private val calendarRepository: CalendarRepository) : ViewModel() {

    private val _addRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val addRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _addRequestLiveData

    fun addSchedule(scheduleReq: ScheduleReq) = viewModelScope.launch {
        _addRequestLiveData.postValue(Resource.loading(null))
        _addRequestLiveData.postValue(calendarRepository.addSchedule(scheduleReq))
    }

}