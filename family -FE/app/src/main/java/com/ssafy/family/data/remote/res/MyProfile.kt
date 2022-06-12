package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyProfile(
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("birthday")
    val birthday: String
): Parcelable