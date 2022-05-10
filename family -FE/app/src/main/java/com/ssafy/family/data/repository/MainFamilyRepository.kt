package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.res.FamilyProfileRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.util.Resource
import retrofit2.Response

interface MainFamilyRepository {
    suspend fun getTodayMission():Resource<MissionRes>
    suspend fun getFamilyProfileList(): Resource<FamilyProfileRes>
}