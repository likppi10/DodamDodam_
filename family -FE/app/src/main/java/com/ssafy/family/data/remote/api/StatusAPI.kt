package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.EditStatusReq
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.data.remote.res.MyStatusRes
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface StatusAPI {

    @GET("/api/family/picture")
    suspend fun getFamilyPicture(): Response<FamilyPictureRes>

    @PATCH("/api/profile/status")
    suspend fun editMyStatus(@Body editStatusReq: EditStatusReq): Response<BaseResponse>

    @Multipart
    @PATCH("/api/family/picture")
    suspend fun editFamilyPicture(@Part picture: MultipartBody.Part?): Response<BaseResponse>

    @GET("/api/profile/condition")
    suspend fun getMyStatus(): Response<MyStatusRes>

}