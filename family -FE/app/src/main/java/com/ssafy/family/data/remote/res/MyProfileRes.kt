package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class MyProfileRes:BaseResponse() {
    @SerializedName("data")
    val data: MyProfile? = null
}