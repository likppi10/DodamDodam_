package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditStatusReq (
    @SerializedName("emotion")
    var emotion: String,
    @SerializedName("comment")
    var comment: String
): Parcelable
