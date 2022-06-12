package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(
    @SerializedName("albumId")
    val albumId: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("imagePath")
    val imagePath: String
): Parcelable
