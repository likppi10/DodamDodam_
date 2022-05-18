package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.SendPushReq
import com.ssafy.family.data.remote.res.AlarmListRes
import com.ssafy.family.data.remote.res.FamilyProfileRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.util.Resource

interface MainFamilyRepository {

    suspend fun getTodayMission():Resource<MissionRes>

    suspend fun getFamilyProfileList(): Resource<FamilyProfileRes>

    suspend fun sendAlarm(sendPushReq: SendPushReq):Resource<BaseResponse>

    suspend fun getAlarmList(targetProfileId: Int): Resource<AlarmListRes>

}