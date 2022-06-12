package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class RefreshTokenRes:BaseResponse(){
    @SerializedName("data")
    val dataSet: RefreshJWT? = null
}
