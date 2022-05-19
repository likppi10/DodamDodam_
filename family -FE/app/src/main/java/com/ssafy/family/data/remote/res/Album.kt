package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    @SerializedName("id")
    val id: Int,
    @SerializedName("family")
    val family: List<Family>,
    @SerializedName("date")
    val date: String
): Parcelable

