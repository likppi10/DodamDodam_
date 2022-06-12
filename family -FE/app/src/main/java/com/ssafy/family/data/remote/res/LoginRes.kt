package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class LoginRes: BaseResponse() {
    @SerializedName("data")
    val dataSet: UserInfo? = null
}