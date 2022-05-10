package com.ssafy.family.ui.startsetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.data.repository.FamilyRepository
import com.ssafy.family.util.Resource
import com.ssafy.family.util.Status
import com.ssafy.family.util.UiMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StartSettingViewModel @Inject constructor(private val familyRepository: FamilyRepository) :
    ViewModel() {

    // 가족 생성/가입 시 받아온 id
    private val _familyResponseLiveData = MutableLiveData<Resource<FamilyRes>>()
    val familyResponseLiveData: LiveData<Resource<FamilyRes>>
        get() = _familyResponseLiveData

    // 가족코드 검증 시 받아온 id
    private val _checkFamilyCodeRes = MutableLiveData<Resource<FamilyRes?>>()
    val checkFamilyCodeRes: LiveData<Resource<FamilyRes?>>
        get() = _checkFamilyCodeRes

    // 가족코드 검증 성공 시 화면 전환을 위한 체크
    private val _isChecked = MutableLiveData<UiMode>()
    val isChecked: LiveData<UiMode>
        get() = _isChecked


    fun createFamily(profile: FamilyReq, imageFile : File?) = viewModelScope.launch {
        _familyResponseLiveData.postValue(Resource.loading(null))
        _familyResponseLiveData.postValue(familyRepository.createFamily(profile, imageFile))
    }

    fun joinFamily(profile: FamilyReq, familyId: Int, imageFile: File?) = viewModelScope.launch {
        _familyResponseLiveData.postValue(Resource.loading(null))
        _familyResponseLiveData.postValue(familyRepository.joinFamily(profile, familyId, imageFile))
    }

    fun checkFamilyCode(familyCode: String) = viewModelScope.launch {
        _checkFamilyCodeRes.postValue(Resource.loading(null))
        val response = familyRepository.checkFamilyCode(familyCode)
        _checkFamilyCodeRes.postValue(response)
        if (response.status == Status.SUCCESS) {
            _isChecked.postValue(UiMode.READY)
        } else if (response.status == Status.ERROR) {
            _isChecked.postValue((UiMode.FAIL))
        }
    }

}