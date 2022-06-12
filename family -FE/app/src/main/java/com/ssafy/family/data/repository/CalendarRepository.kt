package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.ScheduleRes
import com.ssafy.family.data.remote.res.SchedulesRes
import com.ssafy.family.util.Resource

interface CalendarRepository {

    suspend fun getDaySchedule(day:String): Resource<SchedulesRes>

    suspend fun getMonthSchedule(month:String): Resource<SchedulesRes>

    suspend fun getSchedule(scheduleId:Long): Resource<ScheduleRes>

    suspend fun addSchedule(scheduleReq: ScheduleReq): Resource<BaseResponse>

    suspend fun editSchedule(scheduleId:Long, scheduleReq: ScheduleReq): Resource<BaseResponse>

    suspend fun deleteSchedule(scheduleId:Long): Resource<BaseResponse>

}