package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class FamilyProfileRes:BaseResponse() {
    @SerializedName("data")
    val dataSet: List<FamilyProfile>? = null
}