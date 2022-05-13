package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class OpinionReactionRes: BaseResponse() {
    @SerializedName("data")
    val opinionReaction: OpinionReaction? = null
}