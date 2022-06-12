package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    @SerializedName("jwtToken")
    val jwtToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profileId")
    val profileId: Long,
    @SerializedName("familyId")
    val familyId: Long
): Parcelable
