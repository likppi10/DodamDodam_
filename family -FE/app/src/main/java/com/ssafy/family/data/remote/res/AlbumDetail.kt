package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumDetail(
    @SerializedName("date")
    val date: String,
    @SerializedName("pictures")
    val pictures: List<AlbumPicture>,
    @SerializedName("hashtags")
    val hashTags: List<HashTag>,
    @SerializedName("albumReactions")
    val albumReactions: List<AlbumReaction>
) : Parcelable
