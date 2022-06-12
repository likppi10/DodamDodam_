package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.MainEventAPI
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.OpinionRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainEventRepositoryImpl(
    private val api: MainEventAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : MainEventRepository {

    override suspend fun getOpinion(): Resource<OpinionRes>  = withContext(ioDispatcher){
        try {
            val response = api.getOpinion()
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

    override suspend fun addOpinionReaction(opinionReactionReq: OpinionReactionReq): Resource<OpinionRes> = withContext(ioDispatcher) {
        try {
            val response = api.addOpinionReaction(opinionReactionReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 ->{
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

    override suspend fun addOpinion(opinionReq: OpinionReq): Resource<BaseResponse>  = withContext(ioDispatcher){
        try {
            val response = api.addOpinion(opinionReq)
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

    override suspend fun deleteOpinion(suggestionId:Long): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.deleteOpinion(suggestionId)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 ->{
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