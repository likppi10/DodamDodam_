package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class WishInfo (
    @SerializedName("profileImg")
    val canCreate: Boolean,
    @SerializedName("wishTree")
    val wishTree: List<WishTree>
): Parcelable