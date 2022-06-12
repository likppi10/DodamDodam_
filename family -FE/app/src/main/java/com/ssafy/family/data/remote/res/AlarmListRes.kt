package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class AlarmListRes:BaseResponse() {
    @SerializedName("data")
    val dataSet: List<AlarmInfo>? = null
}