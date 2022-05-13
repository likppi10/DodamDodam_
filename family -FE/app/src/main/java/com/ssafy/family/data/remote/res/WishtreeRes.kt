package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ssafy.family.config.BaseResponse
import kotlinx.android.parcel.Parcelize

class WishtreeRes: BaseResponse() {
    @SerializedName("data")
    val dataSet: WishInfo? = null
}