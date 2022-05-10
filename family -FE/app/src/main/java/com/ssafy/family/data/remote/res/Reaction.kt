package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Reaction(
    @SerializedName("profileId")
    val profileId: Long,
    @SerializedName("like")
    val like: Boolean
): Parcelable

