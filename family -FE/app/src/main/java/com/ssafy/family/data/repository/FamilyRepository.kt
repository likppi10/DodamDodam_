package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.util.Resource
import java.io.File

interface FamilyRepository {
    suspend fun createFamily(profile: FamilyReq, imageFile: File?): Resource<FamilyRes>

    suspend fun joinFamily(profile: FamilyReq, familyId: Int, imageFile: File?): Resource<FamilyRes>

    suspend fun checkFamilyCode(familyCode: String): Resource<FamilyRes>
}