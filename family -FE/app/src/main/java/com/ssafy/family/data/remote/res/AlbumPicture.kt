package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumPicture(
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("main")
    var main: Boolean,
    @SerializedName("pictureId")
    var pictureId:Int
): Parcelable


