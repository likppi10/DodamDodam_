package com.ssafy.family.data.repository

import com.google.firebase.database.DatabaseReference
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.ChatData
import com.ssafy.family.data.remote.res.ChattingRes
import com.ssafy.family.util.Resource

interface ChatRepository {

    fun send(data: ChatData, myRef: DatabaseReference): Any

    suspend fun getMember(): Resource<ChattingRes>

    suspend fun sendChattingFCM(text:String):Resource<BaseResponse>

}