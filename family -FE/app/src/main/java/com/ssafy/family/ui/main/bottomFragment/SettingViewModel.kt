package com.ssafy.family.ui.main.bottomFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.FamilyCodeRes
import com.ssafy.family.data.remote.res.ProfileImageRes
import com.ssafy.family.data.remote.res.SchedulesRes
import com.ssafy.family.data.repository.CalendarRepository
import com.ssafy.family.data.repository.SettingRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val settingRepository: SettingRepository) :
    ViewModel() {

    private val _getProfileImageRequestLiveData = MutableLiveData<Resource<ProfileImageRes>>()
    val getProfileImageRequestLiveData: LiveData<Resource<ProfileImageRes>>
        get() = _getProfileImageRequestLiveData

    private val _getFamilyCodeRequestLiveData = MutableLiveData<Resource<FamilyCodeRes>>()
    val getFamilyCodeRequestLiveData: LiveData<Resource<FamilyCodeRes>>
        get() = _getFamilyCodeRequestLiveData

    private val _exitFamilyRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val exitFamilyRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _exitFamilyRequestLiveData

//    private val _getMonthRequestLiveData = MutableLiveData<Resource<SchedulesRes>>()
//    val getMonthRequestLiveData: LiveData<Resource<SchedulesRes>>
//        get() = _getMonthRequestLiveData

    fun getProfileImage() = viewModelScope.launch {
        _getProfileImageRequestLiveData.postValue(Resource.loading(null))
        _getProfileImageRequestLiveData.postValue(settingRepository.getProfileImage())
    }

    fun getFamilyCode() = viewModelScope.launch {
        _getFamilyCodeRequestLiveData.postValue(Resource.loading(null))
        _getFamilyCodeRequestLiveData.postValue(settingRepository.getFamilyCode())
    }

    fun exitFamily() = viewModelScope.launch {
        _exitFamilyRequestLiveData.postValue(Resource.loading(null))
        _exitFamilyRequestLiveData.postValue(settingRepository.exitFamily())
    }

//    fun getProfileImage() = viewModelScope.launch {
//        _getProfileImageRequestLiveData.postValue(Resource.loading(null))
//        _getProfileImageRequestLiveData.postValue(settingRepository.getProfileImage())
//    }

}