package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumReactionReq(
    @SerializedName("emoticon")
    val emoticon: String,
    @SerializedName("albumId")
    val albumId: Int
) : Parcelable


