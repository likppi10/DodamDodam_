package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumReaction(
    @SerializedName("reactionId")
    val reactionId: Int,
    @SerializedName("emoticon")
    val emoticon: String,
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("profileId")
    val profileId: Int
) : Parcelable
