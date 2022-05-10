package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.SendPushReq
import com.ssafy.family.data.remote.res.FamilyProfileRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.util.Resource
import retrofit2.Response
import retrofit2.http.Body

interface MainFamilyRepository {
    suspend fun getTodayMission():Resource<MissionRes>
    suspend fun getFamilyProfileList(): Resource<FamilyProfileRes>
    suspend fun sendAlarm(sendPushReq: SendPushReq):Resource<BaseResponse>
}