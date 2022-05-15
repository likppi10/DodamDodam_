package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class WishInfo (
    @SerializedName("myWishPosition")
    val myWishPosition: Int,
    @SerializedName("wishTree")
    val wishTree: List<WishTree>
): Parcelable