package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FamilyProfile(
    @SerializedName("profileId")
    val profileId: Int,
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("emotion")
    val emotion: String,
    @SerializedName("comment")
    val comment: String
): Parcelable