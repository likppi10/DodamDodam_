package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class ChattingRes: BaseResponse() {
    @SerializedName("data")
    val memberList: List<MemberInfo> = listOf()
}