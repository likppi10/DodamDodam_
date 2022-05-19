package com.ssafy.family.data.remote.api

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.ScheduleReq
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.data.remote.res.SchedulesRes
import retrofit2.Response
import retrofit2.http.*

interface CalendarAPI {

    @GET("/api/schedule/day/{day}")
    suspend fun getDaySchedule(@Path("day") day:String): Response<SchedulesRes>

    @GET("/api/schedule/month/{month}")
    suspend fun getMonthSchedule(@Path("month") month:String): Response<SchedulesRes>

    @GET("/api/schedule/{scheduleId}")
    suspend fun getSchedule(@Path("scheduleId") scheduleId:Long): Response<ScheduleRes>

    @POST("api/schedule/create")
    suspend fun addSchedule(@Body scheduleReq: ScheduleReq): Response<BaseResponse>

    @PATCH("/api/schedule/{scheduleId}")
    suspend fun editSchedule(
        @Path("scheduleId") scheduleId:Long,
        @Body scheduleReq: ScheduleReq
    ): Response<BaseResponse>

    @HTTP(method = "DELETE", path = "/api/schedule/{scheduleId}")
    suspend fun deleteSchedule(@Path("scheduleId") scheduleId:Long): Response<BaseResponse>

}