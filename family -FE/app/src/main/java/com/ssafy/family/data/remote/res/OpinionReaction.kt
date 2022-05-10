package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OpinionReaction(
    @SerializedName("suggestionId")
    val suggestionId: Long,
    @SerializedName("like")
    val like: Int,
    @SerializedName("dislike")
    val dislike: Int
): Parcelable

