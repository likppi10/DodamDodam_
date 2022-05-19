package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.OpinionReactionReq
import com.ssafy.family.data.remote.req.OpinionReq
import com.ssafy.family.data.remote.res.OpinionRes
import retrofit2.Response
import retrofit2.http.*

interface MainEventAPI {

    @GET("/api/main/suggestionList")
    suspend fun getOpinion(): Response<OpinionRes>

    @POST("api/main/suggestion/reaction")
    suspend fun addOpinionReaction(@Body opinionReactionReq: OpinionReactionReq): Response<OpinionRes>

    @POST("api/main/suggestion")
    suspend fun addOpinion(@Body opinionReq: OpinionReq): Response<BaseResponse>

    @HTTP(method = "DELETE", path = "/api/main/{suggestionId}")
    suspend fun deleteOpinion(@Path("suggestionId") suggestionId :Long): Response<BaseResponse>

}