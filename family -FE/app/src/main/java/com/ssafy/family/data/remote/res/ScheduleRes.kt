package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class ScheduleRes: BaseResponse() {
    @SerializedName("data")
    val schedule: ScheduleInfo? = null
}