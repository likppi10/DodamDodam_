package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class WishtreeRes: BaseResponse() {
    @SerializedName("data")
    val dataSet: WishInfo? = null
}