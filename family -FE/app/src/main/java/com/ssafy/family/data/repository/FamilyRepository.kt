package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.data.remote.res.MyProfileRes
import com.ssafy.family.util.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.PartMap
import java.io.File

interface FamilyRepository {
    suspend fun createFamily(profile: FamilyReq, imageFile: File?): Resource<FamilyRes>

    suspend fun joinFamily(profile: FamilyReq, familyId: Int, imageFile: File?): Resource<FamilyRes>

    suspend fun checkFamilyCode(familyCode: String): Resource<FamilyRes>

    suspend fun getMyProfile(): Resource<MyProfileRes>

    suspend fun updateMyProfile(profile: FamilyReq, imageFile: File?): Resource<BaseResponse>
}