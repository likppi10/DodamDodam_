package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ssafy.family.data.remote.res.HashTag
import kotlinx.android.parcel.Parcelize

@Parcelize
class OpinionReactionReq (
    @SerializedName("suggestionId")
    val suggestionId: Long,
    @SerializedName("like")
    val like: Boolean
) : Parcelable
