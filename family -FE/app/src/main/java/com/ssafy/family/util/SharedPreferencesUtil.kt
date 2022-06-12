package com.ssafy.family.util

import android.content.Context
import android.content.SharedPreferences
import com.ssafy.family.config.ApplicationClass

class SharedPreferencesUtil(context: Context) {

    private var preferences: SharedPreferences = context.getSharedPreferences(ApplicationClass.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addUserCookie(cookies: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(ApplicationClass.COOKIES_KEY_NAME, cookies)
        editor.apply()
    }

    fun getUserCookie(): MutableSet<String>? {
        return preferences.getStringSet(ApplicationClass.COOKIES_KEY_NAME, HashSet())
    }

    fun setString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key:String): String? {
        return preferences.getString(key, null)
    }

    fun setAutoLogin(flag: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(ApplicationClass.AUTO_LOGIN, flag)
        editor.apply()
    }

    fun getAutoLoginFlag():Boolean {
        return preferences.getBoolean(ApplicationClass.AUTO_LOGIN, false)
    }

    fun deleteString(key: String) {
        val editor = preferences.edit()
        editor.remove(key).apply()
    }

}