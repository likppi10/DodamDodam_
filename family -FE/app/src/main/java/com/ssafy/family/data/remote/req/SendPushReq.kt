package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendPushReq(
    @SerializedName("targetProfileId")
    val targetProfileId: String,
    @SerializedName("content")
    val content: String
) : Parcelable
