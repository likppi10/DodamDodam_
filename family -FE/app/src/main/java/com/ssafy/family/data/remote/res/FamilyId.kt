package com.ssafy.family.data.remote.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FamilyId(
    @SerializedName("familyId")
    val familyId: Int?
): Parcelable