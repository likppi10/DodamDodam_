package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.EditStatusReq
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.data.remote.res.StatusRes
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface StatusAPI {
    @GET("/api/family/picture")
    suspend fun getFamilyPicture(): Response<FamilyPictureRes>
    @GET("/api/profile/comment")
    suspend fun getMyStatus(): Response<StatusRes>
    @PATCH("/api/profile/status")
    suspend fun editMyStatus(@Body editStatusReq: EditStatusReq): Response<BaseResponse>
    //@Multipart
    //    @POST("/api/family/join")
    //    suspend fun joinFamily(
    //        @PartMap data: HashMap<String, RequestBody>,
    //        @Part image: MultipartBody.Part?
    //    ): Response<FamilyRes>
    @Multipart
    @PUT("/api/family/picture")
    suspend fun editFamilyPicture(
        @Part picture: MultipartBody.Part?
    ): Response<BaseResponse>
}