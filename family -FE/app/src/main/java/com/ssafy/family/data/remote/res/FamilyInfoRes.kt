package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class FamilyInfoRes: BaseResponse() {
    @SerializedName("data")
    val dataset: FamilyInfo? = null
}