package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.*
import com.ssafy.family.data.remote.res.OpinionReactionRes
import com.ssafy.family.data.remote.res.OpinionRes
import com.ssafy.family.util.Resource

interface MainEventRepository {

    suspend fun getOpinion(): Resource<OpinionRes>

    suspend fun addOpinionReaction(opinionReactionReq: OpinionReactionReq): Resource<OpinionRes>

    suspend fun addOpinion(text: String): Resource<BaseResponse>

    suspend fun deleteOpinion(suggestionId:Long): Resource<BaseResponse>
}