package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class FamilyIdRes: BaseResponse() {
    @SerializedName("data")
    val dataset: FamilyId? = null
}