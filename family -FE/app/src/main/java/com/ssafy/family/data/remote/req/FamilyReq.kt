package com.ssafy.family.data.remote.req

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.File

@Parcelize
data class FamilyReq(
    @SerializedName("role")
    val role: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("birthday")
    val birthday: String,
) : Parcelable
