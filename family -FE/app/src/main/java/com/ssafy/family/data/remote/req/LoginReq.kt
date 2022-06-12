package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginReq(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("password")
    val password: String
) : Parcelable

