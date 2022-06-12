package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class AlbumDetailRes:BaseResponse() {
    @SerializedName("data")
    val dataSet: AlbumDetail? = null
}