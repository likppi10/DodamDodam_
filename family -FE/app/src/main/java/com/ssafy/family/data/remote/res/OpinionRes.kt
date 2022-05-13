package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class OpinionRes: BaseResponse() {
    @SerializedName("data")
    val opinions: List<Opinion> = listOf()
}