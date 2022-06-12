package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.data.remote.res.FamilyIdRes
import com.ssafy.family.data.remote.res.FamilyInfoRes
import com.ssafy.family.data.remote.res.MyProfileRes
import com.ssafy.family.util.Resource
import java.io.File

interface FamilyRepository {

    suspend fun createFamily(profile: FamilyReq, imageFile: File?): Resource<FamilyInfoRes>

    suspend fun joinFamily(profile: FamilyReq, familyId: Int, imageFile: File?): Resource<FamilyInfoRes>

    suspend fun checkFamilyCode(familyCode: String): Resource<FamilyIdRes>

    suspend fun getMyProfile(): Resource<MyProfileRes>

    suspend fun updateMyProfile(profile: FamilyReq, imageFile: File?): Resource<BaseResponse>

}