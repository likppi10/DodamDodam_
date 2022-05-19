package com.ssafy.family.data.repository

import android.util.Log
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.data.remote.req.findIdReq
import com.ssafy.family.data.remote.res.LoginRes
import com.ssafy.family.data.remote.res.MissionRes
import com.ssafy.family.data.remote.res.RefreshTokenRes
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val api: AccountAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : AccountRepository {

    override suspend fun login(user: LoginReq): Resource<LoginRes> = withContext(ioDispatcher) {
        try {
            val response = api.login(user)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun socialLogin(): Resource<LoginRes> = withContext(ioDispatcher) {
        try {
            val response = api.socialLogin()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun addFcm(fcmToken: AddFcmReq): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.addFcm(fcmToken)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun signUp(signUpReq: SignUpReq): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.signUp(signUpReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun findId(findIdReq: findIdReq): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.findId(findIdReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun newPassword(newIdPwReq: LoginReq): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.newPassword(newIdPwReq)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun idCheck(userId: String): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.idCheck(userId)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 409 -> {
                    Resource.error(null, "중복된 아이디에요.")
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun updateBirthDay(birthday: String): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.updateBirthDay(birthday)
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun MakeRefreshToken(refreshToken: String): Resource<RefreshTokenRes> = withContext(ioDispatcher) {
        try {
            val response = api.makeRefreshToken(refreshToken)
            when {
                response.isSuccessful -> {
                    Log.d("ddddd", "MakeRefreshToken: "+"토큰리프레쉬")
                    LoginUtil.changeUserToken(response.body()!!.dataSet!!)
                    Resource.success(response.body()!!)
                }
                response.code() == 403 -> {
                    Resource.expired(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun getMainMission(): Resource<MissionRes> = withContext(ioDispatcher) {
        try {
            val response = api.getMainMission()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

    override suspend fun logout(): Resource<BaseResponse> = withContext(ioDispatcher) {
        try {
            val response = api.logout()
            when {
                response.isSuccessful -> {
                    Resource.success(response.body()!!)
                }
                else -> {
                    Resource.error(null, response.message())
                }
            }
        } catch (e: Exception) {
            Resource.error(null, "서버와 연결오류")
        }
    }

}