package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyStatusInfo(
    @SerializedName("emotion")
    val emotion: String,
    @SerializedName("comment")
    val comment: String
): Parcelable
