package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.WishtreeAPI
import com.ssafy.family.data.remote.req.WishTreeReq
import com.ssafy.family.data.remote.res.WishtreeRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class WishtreeRepositoryImpl(
    private val api: WishtreeAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
): WishtreeRepository {

    override suspend fun getWishTree(): Resource<WishtreeRes> = withContext(ioDispatcher) {
        try {
            val response = api.getWishTree()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "통신 에러 $e")
        }
    }

    override suspend fun createWishTree(content: String): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val wishTreeReq = WishTreeReq(content = content)
            val response = api.createWishTree(wishTreeReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "통신 에러 $e")
        }
    }

    override suspend fun updateWishTree(wishTreeId: Int, content: String): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val wishTreeReq = WishTreeReq(content = content)
            val response = api.updateWishTree(wishTreeId, wishTreeReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "통신 에러 $e")
        }
    }

    override suspend fun deleteWishTree(wishTreeId: Int): Resource<BaseResponse> = withContext(ioDispatcher){
        try {
            val response = api.deleteWishTree(wishTreeId)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "통신 에러 $e")
        }
    }

}