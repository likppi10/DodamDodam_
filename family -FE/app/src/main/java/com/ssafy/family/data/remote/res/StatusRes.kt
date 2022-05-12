package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.ScheduleInfo

class StatusRes: BaseResponse() {
    @SerializedName("data")
    val todaysMessage: String? = null
}