package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.ScheduleInfo

class ProfileImageRes: BaseResponse() {
    @SerializedName("data")
    val profileImage: String? = null
}