package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.SettingAPI
import com.ssafy.family.data.remote.res.*
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class SettingRepositoryImpl(
    private val api: SettingAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : SettingRepository {

    override suspend fun getProfileImage(): Resource<ProfileImageRes> = withContext(ioDispatcher){
        try {
            val response = api.getProfileImage()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun getStatus(): Resource<MyStatusRes> = withContext(ioDispatcher){
        try {
            val response = api.getStatus()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun getFamilyCode(): Resource<FamilyCodeRes> = withContext(ioDispatcher){
        try {
            val response = api.getFamilyCode()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun exitFamily(): Resource<BaseResponse> = withContext(ioDispatcher){
        try {
            val response = api.exitFamily()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

}