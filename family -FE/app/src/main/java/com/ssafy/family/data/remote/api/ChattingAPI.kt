package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.ChattingRes
import retrofit2.Response
import retrofit2.http.*

interface ChattingAPI {

    @GET("api/chatting")
    suspend fun getMember():Response<ChattingRes>

    @POST("/api/chatting/send")
    suspend fun sendChattingFCM(@Query("text")text:String):Response<BaseResponse>

}