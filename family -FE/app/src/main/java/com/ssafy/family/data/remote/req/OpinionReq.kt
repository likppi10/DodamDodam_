package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OpinionReq (
    @SerializedName("text")
    val text: String
) : Parcelable
