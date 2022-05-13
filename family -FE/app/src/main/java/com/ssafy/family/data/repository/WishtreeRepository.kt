package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.WishTreeReq
import com.ssafy.family.data.remote.res.WishtreeRes
import com.ssafy.family.util.Resource
import retrofit2.Response

interface WishtreeRepository {
    suspend fun getWishTree(): Resource<WishtreeRes>

    suspend fun createWishTree(content: String): Resource<BaseResponse>

}