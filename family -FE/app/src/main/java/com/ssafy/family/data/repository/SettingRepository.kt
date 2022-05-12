package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.FamilyCodeRes
import com.ssafy.family.data.remote.res.ProfileImageRes
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.data.remote.res.SchedulesRes
import com.ssafy.family.util.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HTTP

interface SettingRepository {

    suspend fun getProfileImage(): Resource<ProfileImageRes>

//    suspend fun getStatus(): Resource<FamilyCodeRes>

    suspend fun getFamilyCode(): Resource<FamilyCodeRes>

    suspend fun exitFamily(): Resource<BaseResponse>

}