package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.ScheduleReq
import com.ssafy.family.data.remote.res.FamilyCodeRes
import com.ssafy.family.data.remote.res.ProfileImageRes
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.data.remote.res.SchedulesRes
import retrofit2.Response
import retrofit2.http.*

interface SettingAPI {

    @GET("/api/profile/image")
    suspend fun getProfileImage(): Response<ProfileImageRes>

    // TODO: 창기님이 주시면 
//    @GET("/api/profile/comment")
//    suspend fun getStatus(): Response<SchedulesRes>

    @GET("/api/family/code")
    suspend fun getFamilyCode(): Response<FamilyCodeRes>

    @HTTP(method = "DELETE", path = "api/profile/delete")
    suspend fun exitFamily(): Response<BaseResponse>
}