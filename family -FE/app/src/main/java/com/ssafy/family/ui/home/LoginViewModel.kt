package com.ssafy.family.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.data.remote.req.findIdReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.data.remote.res.RefreshTokenRes
import com.ssafy.family.data.repository.AccountRepository
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository) : ViewModel() {

    var isAutoLogin = LoginUtil.isAutoLogin()

    private val _loginRequestLiveData = MutableLiveData<Resource<LoginRes>>()
    val loginRequestLiveData: LiveData<Resource<LoginRes>>
        get() = _loginRequestLiveData

    private val _makeRefreshLiveData = MutableLiveData<Resource<RefreshTokenRes>>()
    val makeRefreshLiveData : LiveData<Resource<RefreshTokenRes>>
        get() = _makeRefreshLiveData

    private val _baseResLiveData = MutableLiveData<Resource<BaseResponse>>()
    val baseResponse: LiveData<Resource<BaseResponse>>
        get() = _baseResLiveData

    private val _findLiveData = MutableLiveData<Resource<BaseResponse>>()
    val findLiveData: LiveData<Resource<BaseResponse>>
        get() = _findLiveData

    private val _idCheckLiveData = MutableLiveData<Resource<BaseResponse>>()
    val idCheckLiveData: LiveData<Resource<BaseResponse>>
        get() = _idCheckLiveData

    private val _signUpLiveData = MutableLiveData<Resource<BaseResponse>>()
    val signUpLiveData: LiveData<Resource<BaseResponse>>
        get() = _signUpLiveData

    private val _socialLoginLiveData = MutableLiveData<Resource<LoginRes>>()
    val socialLoginLiveData: LiveData<Resource<LoginRes>>
        get() = _socialLoginLiveData

    // 오늘 첫 로그인 체크
    private val _checkFirstLoginToday = MutableLiveData<Resource<MissionRes>>()
    val checkFirstLoginToday: LiveData<Resource<MissionRes>>
        get() = _checkFirstLoginToday

    fun MakeRefresh(refreshToken:String)= viewModelScope.launch {
        _makeRefreshLiveData.postValue(Resource.loading(null))
        _makeRefreshLiveData.postValue(accountRepository.MakeRefreshToken(refreshToken))
    }
    fun Login(user: LoginReq) = viewModelScope.launch {
        _loginRequestLiveData.postValue(Resource.loading(null))
        val response = accountRepository.login(user)
        _loginRequestLiveData.postValue(response)
    }

    fun addFCM(fcmToken: AddFcmReq) = viewModelScope.launch {
        _baseResLiveData.postValue(Resource.loading(null))
        _baseResLiveData.postValue(accountRepository.addFcm(fcmToken))
    }

    fun findId(findIdReq: findIdReq) = viewModelScope.launch {
        _findLiveData.postValue(Resource.loading(null))
        _findLiveData.postValue(accountRepository.findId(findIdReq))
    }

    fun newPassword(newIdPwReq: LoginReq) = viewModelScope.launch {
        _baseResLiveData.postValue(Resource.loading(null))
        _baseResLiveData.postValue(accountRepository.newPassword(newIdPwReq))
    }

    fun idCheck(userId: String) = viewModelScope.launch {
        _idCheckLiveData.postValue(Resource.loading(null))
        _idCheckLiveData.postValue(accountRepository.idCheck(userId))
    }

    fun signUp(signUpReq: SignUpReq) = viewModelScope.launch {
        _signUpLiveData.postValue(Resource.loading(null))
        _signUpLiveData.postValue(accountRepository.signUp(signUpReq))
    }

    fun getFirstLoginToday() = viewModelScope.launch {
        _checkFirstLoginToday.postValue(Resource.loading(null))
        _checkFirstLoginToday.postValue(accountRepository.getMainMission())
    }

    fun socialLogin() = viewModelScope.launch {
        _socialLoginLiveData.postValue(Resource.loading(null))
        _socialLoginLiveData.postValue(accountRepository.socialLogin())
    }

}