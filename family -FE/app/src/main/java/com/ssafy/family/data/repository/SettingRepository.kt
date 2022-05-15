package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.*
import com.ssafy.family.util.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HTTP

interface SettingRepository {

    suspend fun getProfileImage(): Resource<ProfileImageRes>

    suspend fun getStatus(): Resource<MyStatusRes>

    suspend fun getFamilyCode(): Resource<FamilyCodeRes>

    suspend fun exitFamily(): Resource<BaseResponse>

}