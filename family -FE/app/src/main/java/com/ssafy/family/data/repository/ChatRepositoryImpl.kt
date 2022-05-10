package com.ssafy.family.data.repository

import com.google.firebase.database.DatabaseReference
import com.ssafy.family.data.ChatData
import com.ssafy.family.data.remote.api.ChattingAPI
import com.ssafy.family.data.remote.res.ChattingRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class ChatRepositoryImpl (
    private val api: ChattingAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : ChatRepository{

    override suspend fun send(data: ChatData, myRef: DatabaseReference) = withContext(ioDispatcher){
         try{
             myRef.push().setValue(data)
        }catch (e:Exception){
            Resource.error(null,"서버와 연결오류")
        }
    }

    override suspend fun getMember(): Resource<ChattingRes> = withContext(ioDispatcher){
        try {
            val response = api.getMember()
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