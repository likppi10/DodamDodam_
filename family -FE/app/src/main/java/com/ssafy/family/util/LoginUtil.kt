package com.ssafy.family.util

import com.ssafy.family.config.ApplicationClass.Companion.JWT
import com.ssafy.family.config.ApplicationClass.Companion.sSharedPreferences
import com.ssafy.family.data.remote.res.RefreshJWT
import com.ssafy.family.data.remote.res.UserInfo

object LoginUtil {

    val REFRESH_TOKEN = "refreshToken"
    val NAME = "name"
    val PROFILE_ID = "profileId"
    val FAMILY_ID = "familyId"

    private val preferences = sSharedPreferences

    fun isAutoLogin(): Boolean {
        return preferences.getAutoLoginFlag()
    }

    fun signOut() {
        preferences.deleteString(JWT)
        deleteUserInfo()
    }

    fun setAutoLogin(flag: Boolean) {
        preferences.setAutoLogin(flag)
    }

    fun isTokenExisted(): Boolean {
        return !preferences.getString(JWT).isNullOrBlank()
    }

    fun setScocialToken(socialtoken: String){
        preferences.setString(JWT, socialtoken)
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
        preferences.setString(FAMILY_ID, "0")
    }

    fun getUserInfo(): UserInfo? {
        val accessToken = preferences.getString(JWT)
        val refreshToken = preferences.getString(REFRESH_TOKEN)
        val name = preferences.getString(NAME)
        val profileId = preferences.getString(PROFILE_ID)?.toLong()
        val familyId = preferences.getString(FAMILY_ID)?.toLong()

        return if (accessToken.isNullOrBlank() or refreshToken.isNullOrBlank() or name.isNullOrBlank() or (profileId == null) or (familyId == null)) {
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

    fun setProfileId(id: String) {
        preferences.setString(PROFILE_ID, id)
    }

    fun getProfileId(): String? {
        return preferences.getString(PROFILE_ID)
    }

}