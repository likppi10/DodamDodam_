package com.ssafy.family.ui.main.bottomFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.FamilyCodeRes
import com.ssafy.family.data.remote.res.MyStatusRes
import com.ssafy.family.data.remote.res.ProfileImageRes
import com.ssafy.family.data.repository.AccountRepository
import com.ssafy.family.data.repository.SettingRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val settingRepository: SettingRepository, private val accountRepository: AccountRepository) : ViewModel() {

    private val _getProfileImageRequestLiveData = MutableLiveData<Resource<ProfileImageRes>>()
    val getProfileImageRequestLiveData: LiveData<Resource<ProfileImageRes>>
        get() = _getProfileImageRequestLiveData

    private val _getFamilyCodeRequestLiveData = MutableLiveData<Resource<FamilyCodeRes>>()
    val getFamilyCodeRequestLiveData: LiveData<Resource<FamilyCodeRes>>
        get() = _getFamilyCodeRequestLiveData

    private val _exitFamilyRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val exitFamilyRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _exitFamilyRequestLiveData

    private val _logoutRequestLiveData = MutableLiveData<Resource<BaseResponse>>()
    val logoutRequestLiveData: LiveData<Resource<BaseResponse>>
        get() = _logoutRequestLiveData

    private val _getStatusRequestLiveData = MutableLiveData<Resource<MyStatusRes>>()
    val getStatusRequestLiveData: LiveData<Resource<MyStatusRes>>
        get() = _getStatusRequestLiveData

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

    fun getStatus() = viewModelScope.launch {
        _getStatusRequestLiveData.postValue(Resource.loading(null))
        _getStatusRequestLiveData.postValue(settingRepository.getStatus())
    }

    fun logout() = viewModelScope.launch {
        _logoutRequestLiveData.postValue(Resource.loading(null))
        _logoutRequestLiveData.postValue(accountRepository.logout())
    }

}