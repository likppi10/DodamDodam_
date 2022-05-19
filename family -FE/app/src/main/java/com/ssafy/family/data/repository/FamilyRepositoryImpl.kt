package com.ssafy.family.data.repository

import android.util.Log
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.FamilyAPI
import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.data.remote.res.FamilyIdRes
import com.ssafy.family.data.remote.res.FamilyInfoRes
import com.ssafy.family.data.remote.res.MyProfileRes
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

    override suspend fun createFamily(profile: FamilyReq, imageFile: File?): Resource<FamilyInfoRes> = withContext(ioDispatcher) {
        try {
            val map = HashMap<String, RequestBody>()
            map.put("role", getRequestBody(profile.role))
            map.put("nickname", getRequestBody(profile.nickname))
            map.put("birthday", getRequestBody(profile.birthday))
            if (profile.characterPath != null) {
                map.put("characterPath", getRequestBody(profile.characterPath))
            }
            val image = convertFileToMultipart(imageFile)
            val response = api.createFamily(data = map, image)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 409 -> {
                    Resource.error(null, "이미 가족이 선택한 역할이에요.")
                }
                response.code() == 410 -> {
                    Resource.error(null, "중복된 애칭이에요.")
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "통신 에러 $e")
        }
    }


    override suspend fun joinFamily(profile: FamilyReq, familyId: Int, imageFile: File?): Resource<FamilyInfoRes> = withContext(ioDispatcher) {
        try {
            val map = HashMap<String, RequestBody>()
            map.put("role", getRequestBody(profile.role))
            map.put("nickname", getRequestBody(profile.nickname))
            map.put("birthday", getRequestBody(profile.birthday))
            map.put("familyId", getRequestBody(familyId))
            if (profile.characterPath != null) {
                map.put("characterPath", getRequestBody(profile.characterPath))
            }
            val image = convertFileToMultipart(imageFile)
            val response = api.joinFamily(data = map, image)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 409 -> {
                    Resource.error(null, "이미 가족이 선택한 역할이에요.")
                }
                response.code() == 410 -> {
                    Resource.error(null, "중복된 애칭이에요.")
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버 에러")
        }
    }

    override suspend fun checkFamilyCode(familyCode: String): Resource<FamilyIdRes> = withContext(ioDispatcher){
        try {
            val response = api.checkFamilyCode(familyCode)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버 에러 $e")
        }
    }

    override suspend fun getMyProfile(): Resource<MyProfileRes> = withContext(ioDispatcher){
        try {
            val response = api.getMyProfile()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버 에러 $e")
        }
    }

    override suspend fun updateMyProfile(profile: FamilyReq, imageFile: File?): Resource<BaseResponse> = withContext(ioDispatcher){
        try {
            val map = HashMap<String, RequestBody>()
            map.put("role", getRequestBody(profile.role))
            map.put("nickname", getRequestBody(profile.nickname))
            map.put("birthday", getRequestBody(profile.birthday))
            if (profile.characterPath != null) {
                map.put("characterPath", getRequestBody(profile.characterPath))
            }
            val image = convertFile(imageFile)
            val response = api.updateMyProfile(map, image)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 409 -> {
                    Resource.error(null, "이미 가족이 선택한 역할이에요.")
                }
                response.code() == 410 -> {
                    Resource.error(null, "중복된 애칭이에요.")
                }
                else -> {
                    Resource.error(null, "응답 에러")
                }
            }
        } catch (e: Exception) {
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

    private fun getRequestBody(value: Any): RequestBody {
        return value.toString().toRequestBody("text/plain".toMediaTypeOrNull());
    }

}