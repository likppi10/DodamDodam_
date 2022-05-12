package com.ssafy.family.data.remote.res

import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse

class FamilyPictureRes: BaseResponse() {
    @SerializedName("data")
    val dataset: FamilyPicture? = null
}