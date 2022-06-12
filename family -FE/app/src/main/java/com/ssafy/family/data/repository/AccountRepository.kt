package com.ssafy.family.data.repository

import  com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.data.remote.req.findIdReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.data.remote.res.RefreshTokenRes
import com.ssafy.family.util.Resource

interface AccountRepository {

    suspend fun login(user: LoginReq): Resource<LoginRes>

    suspend fun addFcm(fcmToken:AddFcmReq): Resource<BaseResponse>

    suspend fun signUp(signUpReq: SignUpReq):Resource<BaseResponse>

    suspend fun findId(findIdReq: findIdReq):Resource<BaseResponse>

    suspend fun newPassword(newIdPwReq:LoginReq):Resource<BaseResponse>

    suspend fun idCheck(userId:String):Resource<BaseResponse>

    suspend fun updateBirthDay(birthday:String):Resource<BaseResponse>

    suspend fun MakeRefreshToken(refreshToken:String):Resource<RefreshTokenRes>

    suspend fun getMainMission(): Resource<MissionRes>

    suspend fun socialLogin(): Resource<LoginRes>

    suspend fun logout(): Resource<BaseResponse>

}