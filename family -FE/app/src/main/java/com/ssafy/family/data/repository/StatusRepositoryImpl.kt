package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.StatusAPI
import com.ssafy.family.data.remote.req.EditStatusReq
import com.ssafy.family.data.remote.res.FamilyPictureRes
import com.ssafy.family.data.remote.res.MyStatusRes
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception

class StatusRepositoryImpl(
    private val api: StatusAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : StatusRepository {

    override suspend fun getFamilyPicture(): Resource<FamilyPictureRes> = withContext(ioDispatcher) {
        try {
            val response = api.getFamilyPicture()
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

    override suspend fun getMyStatus(): Resource<MyStatusRes> = withContext(ioDispatcher) {
        try {
            val response = api.getMyStatus()
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

    override suspend fun editMyStatus(emotion: String, comment: String): Resource<BaseResponse> = withContext(ioDispatcher) {
        val request = EditStatusReq(emotion, comment)
        try {
            val response = api.editMyStatus(request)
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

    override suspend fun editFamilyPicture(imageFile: File?): Resource<BaseResponse> = withContext(ioDispatcher) {
        try{
            val familyPicture = convertFileToMultipart(imageFile)
            val response = api.editFamilyPicture(familyPicture)
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

    private fun convertFileToMultipart(file: File?): MultipartBody.Part? {
        if (file == null) {
            return null
        } else {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("picture", file.name, requestFile)
        }
    }

}
