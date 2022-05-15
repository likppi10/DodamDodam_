package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class ProfileImageRes: BaseResponse() {
    @SerializedName("data")
    val profileImage: String? = null
}