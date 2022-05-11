package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FamilyPicture (
    @SerializedName("picture")
    val familyPicture: String?
): Parcelable