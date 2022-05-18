package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.SendPushReq
import com.ssafy.family.data.remote.res.AlarmListRes
import com.ssafy.family.data.remote.res.FamilyProfileRes
import com.ssafy.family.data.remote.res.MissionRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MainFamilyAPI {

    @GET("/api/main/mission")
    suspend fun getTodayMission():Response<MissionRes>

    @GET("/api/main/profileList")
    suspend fun getFamilyProfileList():Response<FamilyProfileRes>

    @POST("/api/main/alarm")
    suspend fun sendAlarm(@Body sendPushReq: SendPushReq):Response<BaseResponse>

    @GET("/api/main/alarm/{targetProfileId}")
    suspend fun getAlarmList(@Path("targetProfileId") targetProfileId: Int):Response<AlarmListRes>

}