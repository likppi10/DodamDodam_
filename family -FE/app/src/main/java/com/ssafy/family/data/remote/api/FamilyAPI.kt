package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.FamilyIdRes
import com.ssafy.family.data.remote.res.FamilyInfoRes
import com.ssafy.family.data.remote.res.MyProfileRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FamilyAPI {

    @Multipart
    @POST("/api/family/create")
    suspend fun createFamily(
        @PartMap data: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<FamilyInfoRes>

    @Multipart
    @POST("/api/family/join")
    suspend fun joinFamily(
        @PartMap data: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<FamilyInfoRes>

    @GET("api/family/code/check/{code}")
    suspend fun checkFamilyCode(@Path("code") code:String):Response<FamilyIdRes>

    @GET("api/profile/myprofile")
    suspend fun getMyProfile():Response<MyProfileRes>

    @Multipart
    @PATCH("/api/profile")
    suspend fun updateMyProfile(
        @PartMap data: HashMap<String, RequestBody>,
        @Part multipartFile: MultipartBody.Part?
    ): Response<BaseResponse>

}