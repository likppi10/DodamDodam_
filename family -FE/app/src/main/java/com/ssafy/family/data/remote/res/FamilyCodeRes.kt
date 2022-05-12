package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.ScheduleInfo

class FamilyCodeRes: BaseResponse() {
    @SerializedName("data")
    val data: FamilyCodeInfo? = null
}