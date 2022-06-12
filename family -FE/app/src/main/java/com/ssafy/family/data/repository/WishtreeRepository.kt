package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.WishtreeRes
import com.ssafy.family.util.Resource

interface WishtreeRepository {

    suspend fun getWishTree(): Resource<WishtreeRes>

    suspend fun createWishTree(content: String): Resource<BaseResponse>

    suspend fun updateWishTree(wishTreeId: Int, content: String): Resource<BaseResponse>

    suspend fun deleteWishTree(wishTreeId: Int): Resource<BaseResponse>

}