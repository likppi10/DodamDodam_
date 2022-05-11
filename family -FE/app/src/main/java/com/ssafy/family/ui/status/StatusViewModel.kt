package com.ssafy.family.ui.status

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.data.remote.res.StatusRes
import com.ssafy.family.data.repository.StatusRepository
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(private val statusRepository: StatusRepository) :
    ViewModel() {
    // 가족사진
    private val _familyPicture = MutableLiveData<Resource<FamilyPictureRes>>()
    val familyPicture: LiveData<Resource<FamilyPictureRes>>
        get() = _familyPicture
    // 가족사진 변경 res
    private val _editFamilyPictureResponse = MutableLiveData<Resource<BaseResponse>>()
    val editFamilyPictureResponse: LiveData<Resource<BaseResponse>>
        get() = _editFamilyPictureResponse
    // 오늘의 한마디
    private val _todaysMessage = MutableLiveData<Resource<StatusRes>>()
    val todaysMessage: LiveData<Resource<StatusRes>>
        get() = _todaysMessage
    // 수정 요청 res
    private val _editStatusResponse = MutableLiveData<Resource<BaseResponse>>()
    val editStatusResponse: LiveData<Resource<BaseResponse>>
        get() = _editStatusResponse
    // 가족사진 선택 페이지 선택된 가족사진 URI
    private val _selectedImgUri = MutableLiveData<Uri?>()
    val selectedImgUri: LiveData<Uri?>
        get() = _selectedImgUri

    fun getFamilyPicture() = viewModelScope.launch {
        _familyPicture.postValue(Resource.loading(null))
        val familyPictureRes = statusRepository.getFamilyPicture()
        _familyPicture.postValue(familyPictureRes)
        if (familyPictureRes.data?.dataset?.familyPicture != null) {
            setImgUri(Uri.parse(familyPictureRes.data.dataset.familyPicture))
        } else {
            setImgUri(null)
        }
        Log.d(TAG, "StatusViewModel - getFamilyPicture() called ${familyPictureRes.data?.dataset?.familyPicture}")
    }

    fun getMyStatus() = viewModelScope.launch {
        _todaysMessage.postValue(Resource.loading(null))
        val res = statusRepository.getMyStatus()
        _todaysMessage.postValue(res)
        Log.d(TAG, "StatusViewModel - getMyStatus() $res")
    }

    fun editMyStatus(emotion: String, comment: String) = viewModelScope.launch {
        _editStatusResponse.postValue(Resource.loading(null))
        _editStatusResponse.postValue(statusRepository.editMyStatus(emotion = emotion, comment = comment))
    }

    // 선택된 가족사진 uri 제거
    fun deleteImgUri() = viewModelScope.launch {
        _selectedImgUri.postValue(null)
    }
    // 가족사진 선택
    fun setImgUri(uri: Uri?) = viewModelScope.launch {
        _selectedImgUri.postValue(uri)
    }
    // 가족사진 변경
    fun editFamilyPicture(imageFile: File?) = viewModelScope.launch {
        _editFamilyPictureResponse.postValue(Resource.loading(null))
//        _editFamilyPictureResponse.postValue(statusRepository.editFamilyPicture(imageFile))
        val res = statusRepository.editFamilyPicture(imageFile)
        Log.d(TAG, "StatusViewModel - editFamilyPicture() imageFile : $imageFile")
        Log.d(TAG, "StatusViewModel - editFamilyPicture() res : $res")
        _editFamilyPictureResponse.postValue(res)
    }


    // 가족사진 uri 초기화는 할필요없음 -> 설정에서 가족사진변경 이동 시 엑티비티가 바뀌니까
//    fun resetImgUri() {
//        val familyPicture = familyPicture.value?.data?.dataset?.familyPicture
//        if (familyPicture != null) {
//            _selectedImgUri.postValue(Uri.parse(familyPicture))
//        } else {
//            _selectedImgUri.postValue(null)
//        }
//    }
}