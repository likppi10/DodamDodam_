package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class MyStatusRes: BaseResponse() {
    @SerializedName("data")
    val data: MyStatusInfo? = null
}