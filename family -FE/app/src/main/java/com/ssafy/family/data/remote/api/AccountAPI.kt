package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.data.remote.req.findIdReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.data.remote.res.RefreshTokenRes
import retrofit2.Response
import retrofit2.http.*

interface AccountAPI {

    @POST("/api/user/signin")
    suspend fun login(@Body user : LoginReq):Response<LoginRes>

    @POST("/api/user/fcm")
    suspend fun addFcm(@Body fcmToken:AddFcmReq):Response<BaseResponse>

    @POST("/api/user/signup")
    suspend fun signUp(@Body signUpReq: SignUpReq):Response<BaseResponse>

    @POST("/api/user/findId")
    suspend fun findId(@Body findIdReq: findIdReq):Response<BaseResponse>

    @POST("/api/user/newpassword")
    suspend fun newPassword(@Body newIdPwReq:LoginReq):Response<BaseResponse>

    @GET("/api/user/{userId}")
    suspend fun idCheck(@Path("userId") userId:String):Response<BaseResponse>

    @GET("api/user/birthday/{birthday}")
    suspend fun updateBirthDay(@Path("birthday")birthday:String):Response<BaseResponse>

    @POST("/api/user/refresh")
    suspend fun makeRefreshToken(@Header("X-AUTH-REFRESH-TOKEN") refreshToken:String):Response<RefreshTokenRes>

    @GET("/api/main/mission")
    suspend fun getMainMission(): Response<MissionRes>

    @POST("/api/user/social")
    suspend fun socialLogin():Response<LoginRes>

    @GET("/api/user/signout")
    suspend fun logout():Response<BaseResponse>

}