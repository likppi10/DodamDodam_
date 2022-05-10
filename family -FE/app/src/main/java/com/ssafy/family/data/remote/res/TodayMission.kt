package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodayMission(
    @SerializedName("missionContent")
    val missionContent: String,
    @SerializedName("missionTarget")
    val missionTarget: String
) : Parcelable