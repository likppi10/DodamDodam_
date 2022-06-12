package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class WishTree (
    @SerializedName("position")
    val position: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("profileImage")
    val profileImage: String?,
    @SerializedName("wishTreeId")
    val wishTreeId: Int
): Parcelable