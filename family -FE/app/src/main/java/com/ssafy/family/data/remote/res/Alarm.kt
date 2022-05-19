package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Alarm(
    @SerializedName("familyProfile")
    val familyProfile: FamilyProfile,
    @SerializedName("text")
    val text: String
) : Parcelable

