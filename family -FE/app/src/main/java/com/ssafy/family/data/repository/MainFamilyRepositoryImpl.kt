package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.MainFamilyAPI
import com.ssafy.family.data.remote.req.SendPushReq
import com.ssafy.family.data.remote.res.AlarmListRes
import com.ssafy.family.data.remote.res.FamilyProfileRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MainFamilyRepositoryImpl(
    private val api: MainFamilyAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : MainFamilyRepository {

    override suspend fun getTodayMission(): Resource<MissionRes> = withContext(ioDispatcher) {
        try {
            val response = api.getTodayMission()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun getFamilyProfileList(): Resource<FamilyProfileRes> = withContext(ioDispatcher) {
        try {
            val response = api.getFamilyProfileList()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun sendAlarm(sendPushReq: SendPushReq): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.sendAlarm(sendPushReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun getAlarmList(targetProfileId: Int): Resource<AlarmListRes> = withContext(ioDispatcher) {
        try {
            val response = api.getAlarmList(targetProfileId)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: java.lang.Exception) {
            Resource.error(null, "통신 에러 $e")
        }
    }

}