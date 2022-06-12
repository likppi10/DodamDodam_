package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.*
import retrofit2.Response
import retrofit2.http.*

interface SettingAPI {

    @GET("/api/profile/image")
    suspend fun getProfileImage(): Response<ProfileImageRes>

    @GET("/api/profile/condition")
    suspend fun getStatus(): Response<MyStatusRes>

    @GET("/api/family/code")
    suspend fun getFamilyCode(): Response<FamilyCodeRes>

    @HTTP(method = "DELETE", path = "api/profile/delete")
    suspend fun exitFamily(): Response<BaseResponse>

}