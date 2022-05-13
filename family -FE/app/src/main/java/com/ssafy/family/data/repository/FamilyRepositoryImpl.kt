package com.ssafy.family.data.repository

import android.util.Log
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.FamilyAPI
import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.data.remote.res.FamilyRes
import com.ssafy.family.data.remote.res.MyProfileRes
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception

class FamilyRepositoryImpl(
    private val api: FamilyAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
): FamilyRepository {
    override suspend fun createFamily(
        profile: FamilyReq,
        imageFile: File?
    ): Resource<FamilyRes> =
        withContext(ioDispatcher) {
            try {
                val map = HashMap<String, RequestBody>()
                map.put("role", getRequestBody(profile.role))
                map.put("nickname", getRequestBody(profile.nickname))
                map.put("birthday", getRequestBody(profile.birthday))
                val image = convertFileToMultipart(imageFile)
                val response = api.createFamily(data = map, image)
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

    override suspend fun joinFamily(profile: FamilyReq, familyId: Int, imageFile: File?): Resource<FamilyRes> =
        withContext(ioDispatcher) {
            try {
                val map = HashMap<String, RequestBody>()
                map.put("role", getRequestBody(profile.role))
                map.put("nickname", getRequestBody(profile.nickname))
                map.put("birthday", getRequestBody(profile.birthday))
                map.put("familyId", getRequestBody(familyId))
                val image = convertFileToMultipart(imageFile)
                val response = api.joinFamily(data = map, image)
                when {
                    response.isSuccessful -> {
                        Resource.success(response.body()!!)
                    }
                    else -> {
                        Resource.error(null, "응답 에러")
                    }
                }
            } catch (e: Exception) {
                Resource.error(null, "서버 에러")
            }
        }

    override suspend fun checkFamilyCode(familyCode: String): Resource<FamilyRes> =
        withContext(ioDispatcher){
            try {
                val response = api.checkFamilyCode(familyCode)
                when {
                    response.isSuccessful -> {
                        Log.d(TAG, "FamilyRepositoryImpl - checkFamilyCode() ${Resource.success(response.body()!!)}")
                        Resource.success(response.body()!!)
                    }
                    else -> {
                        Log.d(
                            TAG,
                            "FamilyRepositoryImpl - createFamily() ErrorCode:${
                                response.code().toString()
                            }"
                        )
                        Resource.error(null, "응답 에러")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "api 요청 실패")
                Resource.error(null, "서버 에러 $e")
            }
        }

    override suspend fun getMyProfile(): Resource<MyProfileRes> =
        withContext(ioDispatcher){
            try {
                val response = api.getMyProfile()
                when {
                    response.isSuccessful -> {
                        Log.d(TAG, "FamilyRepositoryImpl - checkFamilyCode() ${Resource.success(response.body()!!)}")
                        Resource.success(response.body()!!)
                    }
                    else -> {
                        Log.d(
                            TAG,
                            "FamilyRepositoryImpl - createFamily() ErrorCode:${
                                response.code().toString()
                            }"
                        )
                        Resource.error(null, "응답 에러")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "api 요청 실패")
                Resource.error(null, "서버 에러 $e")
            }
        }

    override suspend fun updateMyProfile(
        profile: FamilyReq,
        imageFile: File?
    ): Resource<BaseResponse> =
        withContext(ioDispatcher){
            try {
                val map = HashMap<String, RequestBody>()
                map.put("role", getRequestBody(profile.role))
                map.put("nickname", getRequestBody(profile.nickname))
                map.put("birthday", getRequestBody(profile.birthday))
                val image = convertFile(imageFile)
                val response = api.updateMyProfile(map, image)
                when {
                    response.isSuccessful -> {
                        Log.d(TAG, "FamilyRepositoryImpl - checkFamilyCode() ${Resource.success(response.body()!!)}")
                        Resource.success(response.body()!!)
                    }
                    else -> {
                        Log.d(
                            TAG,
                            "FamilyRepositoryImpl - createFamily() ErrorCode:${
                                response.code().toString()
                            }"
                        )
                        Resource.error(null, "응답 에러")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "api 요청 실패")
                Resource.error(null, "서버 에러 $e")
            }
        }

    private fun convertFileToMultipart(file: File?): MultipartBody.Part? {
        if (file == null) {
            return null
        } else {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
    }

    private fun convertFile(file: File?): MultipartBody.Part? {
        if (file == null) {
            return null
        } else {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("multipartFile", file.name, requestFile)
        }
    }

    private fun getRequestBody(value: Any): RequestBody{
        return value.toString().toRequestBody("text/plain".toMediaTypeOrNull());
    }
}