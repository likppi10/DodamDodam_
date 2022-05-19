package com.ssafy.family.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.SendPushReq
import com.ssafy.family.data.remote.res.AlarmListRes
import com.ssafy.family.data.remote.res.FamilyProfileRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.data.repository.MainFamilyRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFamilyViewModel @Inject constructor(private val mainFamilyRepository: MainFamilyRepository) : ViewModel() {

    private val _todayMissionRequestLiveData = MutableLiveData<Resource<MissionRes>>()
    val todayMissionRequestLiveData: LiveData<Resource<MissionRes>>
        get() = _todayMissionRequestLiveData

    private val _familyProfileRequestLiveData = MutableLiveData<Resource<FamilyProfileRes>>()
    val familyProfileRequestLiveData: LiveData<Resource<FamilyProfileRes>>
        get() = _familyProfileRequestLiveData

    private val _sendAlarmRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val sendAlarmRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _sendAlarmRequestLiveData

    private val _getAlarmListRequestLiveData = MutableLiveData<Resource<AlarmListRes>>()
    val getAlarmListRequestLiveData: LiveData<Resource<AlarmListRes>>
        get() = _getAlarmListRequestLiveData

    fun getTodayMission() = viewModelScope.launch {
        _todayMissionRequestLiveData.postValue(Resource.loading(null))
        _todayMissionRequestLiveData.postValue(mainFamilyRepository.getTodayMission())
    }

    fun getFamilyProfiles()=viewModelScope.launch {
        _familyProfileRequestLiveData.postValue(Resource.loading(null))
        _familyProfileRequestLiveData.postValue(mainFamilyRepository.getFamilyProfileList())
    }

    fun sendAlarm(sendPushReq: SendPushReq) = viewModelScope.launch {
        _sendAlarmRequestLiveData.postValue(Resource.loading(null))
        _sendAlarmRequestLiveData.postValue(mainFamilyRepository.sendAlarm(sendPushReq))
    }

    fun getAlarmList(targetProfileId: Int) = viewModelScope.launch {
        _getAlarmListRequestLiveData.postValue(Resource.loading(null))
        _getAlarmListRequestLiveData.postValue(mainFamilyRepository.getAlarmList(targetProfileId))
    }

}