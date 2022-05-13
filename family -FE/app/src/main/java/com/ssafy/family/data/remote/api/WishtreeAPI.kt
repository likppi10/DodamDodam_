package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.WishTreeReq
import com.ssafy.family.data.remote.res.WishtreeRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WishtreeAPI {
    @GET("/api/event/wish-tree")
    suspend fun getWishTree(): Response<WishtreeRes>

    @POST("/api/event/wish-tree")
    suspend fun createWishTree(@Body wishTreeReq: WishTreeReq): Response<BaseResponse>
}