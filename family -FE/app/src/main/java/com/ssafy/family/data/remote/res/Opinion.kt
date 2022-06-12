package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Opinion(
    @SerializedName("suggestionId")
    val suggestionId: Long,
    @SerializedName("text")
    val text: String,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("dislikeCount")
    val dislikeCount: Int,
    @SerializedName("suggestionReactions")
    val suggestionReactions: List<Reaction>
): Parcelable

