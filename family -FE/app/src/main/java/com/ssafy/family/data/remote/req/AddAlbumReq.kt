package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ssafy.family.data.remote.res.HashTag
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddAlbumReq(
    @SerializedName("hashTags")
    val hashTags: List<HashTag>,
    @SerializedName("date")
    val date: String,
    @SerializedName("mainIndex")
    val mainIndex: Int
) : Parcelable

