package com.ssafy.family.util

import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.config.ApplicationClass.Companion.JWT
import com.ssafy.family.data.remote.res.RefreshJWT
import com.ssafy.family.data.remote.res.UserInfo

object LoginUtil {
    val REFRESH_TOKEN = "refreshToken"
    val NAME = "name"
    val PROFILE_ID = "profileId"
    val FAMILY_ID = "familyId"

    private val preferences = ApplicationClass.sSharedPreferences

    fun isAutoLogin(): Boolean {
        return preferences.getAutoLoginFlag()
    }

    fun signOut() {
        preferences.deleteString(ApplicationClass.JWT)
        deleteUserInfo()
    }

    fun deleteFamily() {
        deleteUserFamilyInfo()
    }

    fun setAutoLogin(flag: Boolean) {
        preferences.setAutoLogin(flag)
    }

    fun isTokenExisted(): Boolean {
        return !preferences.getString(ApplicationClass.JWT).isNullOrBlank()
    }

    fun changeUserToken(refreshJWT: RefreshJWT){
        preferences.setString(JWT, refreshJWT.jwtToken)
        preferences.setString(REFRESH_TOKEN, refreshJWT.refreshToken)
    }
    fun saveUserInfo(userInfo: UserInfo) {

        preferences.setString(JWT, userInfo.jwtToken)
        preferences.setString(REFRESH_TOKEN, userInfo.refreshToken)
        preferences.setString(NAME, userInfo.name)
        preferences.setString(PROFILE_ID, userInfo.profileId.toString())
        preferences.setString(FAMILY_ID, userInfo.familyId.toString())
    }

    fun deleteUserInfo() {
        preferences.deleteString(JWT)
        preferences.deleteString(REFRESH_TOKEN)
        preferences.deleteString(NAME)
        preferences.deleteString(PROFILE_ID)
        preferences.deleteString(FAMILY_ID)
    }

    fun deleteUserFamilyInfo() {
//        preferences.deleteString(PROFILE_ID)
        preferences.deleteString(FAMILY_ID)
    }

    fun getUserInfo(): UserInfo? {
        val accessToken = preferences.getString(JWT)
        val refreshToken = preferences.getString(REFRESH_TOKEN)
        val name = preferences.getString(NAME)
        val profileId = preferences.getString(PROFILE_ID)?.toLong()
        val familyId = preferences.getString(FAMILY_ID)?.toLong()

        return if (accessToken!!.isNotEmpty() and refreshToken!!.isNotEmpty() and name!!.isNotEmpty() and (profileId != 0L) and (familyId == null)) {
            UserInfo(accessToken, refreshToken, name, profileId!!, 0)
        }else if (accessToken.isNullOrBlank() or refreshToken.isNullOrBlank() or name.isNullOrBlank() or (profileId == null) or (familyId == null)) {
            null
        } else {
            UserInfo(accessToken!!, refreshToken!!, name!!, profileId!!, familyId!!)
        }
    }

    fun setFamilyId(id: String) {
        preferences.setString(FAMILY_ID, id)
    }

    fun getFamilyId(): String? {
        return preferences.getString(FAMILY_ID)
    }
}