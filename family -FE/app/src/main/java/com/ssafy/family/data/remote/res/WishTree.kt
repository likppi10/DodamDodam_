package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class WishTree (
    @SerializedName("position")
    val position: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("profileImg")
    val profileImg: String?,
    @SerializedName("wishListId")
    val wishListId: Long
): Parcelable