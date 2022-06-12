package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.WishTreeReq
import com.ssafy.family.data.remote.res.WishtreeRes
import retrofit2.Response
import retrofit2.http.*

interface WishtreeAPI {

    @GET("/api/event/wish-tree")
    suspend fun getWishTree(): Response<WishtreeRes>

    @POST("/api/event/wish-tree")
    suspend fun createWishTree(@Body wishTreeReq: WishTreeReq): Response<BaseResponse>

    @PATCH("/api/event/wish-tree/{wishTreeId}")
    suspend fun updateWishTree(
        @Path("wishTreeId") wishTreeId: Int,
        @Body wishTreeReq: WishTreeReq
    ): Response<BaseResponse>

    @DELETE("/api/event/wish-tree/{wishTreeId}")
    suspend fun deleteWishTree(@Path("wishTreeId") wishTreeId: Int): Response<BaseResponse>

}