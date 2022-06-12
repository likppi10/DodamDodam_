package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class AlbumRes : BaseResponse(){
    @SerializedName("data")
    val dataSet: List<AllAlbum>? = null
}