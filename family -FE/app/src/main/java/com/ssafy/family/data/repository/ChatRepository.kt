package com.ssafy.family.data.repository

import com.google.firebase.database.DatabaseReference
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.ChatData
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.res.ChattingRes
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.util.Resource
import retrofit2.Response

interface ChatRepository {
    suspend fun send(data: ChatData, myRef: DatabaseReference): Any

    suspend fun getMember(): Resource<ChattingRes>
}