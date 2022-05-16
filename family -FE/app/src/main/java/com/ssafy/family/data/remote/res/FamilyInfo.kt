package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FamilyInfo(
    @SerializedName("familyId")
    val familyId: Int?,
    @SerializedName("profileId")
    val profileId: Int?
): Parcelable