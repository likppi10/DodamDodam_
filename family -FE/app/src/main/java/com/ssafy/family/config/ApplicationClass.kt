package com.ssafy.family.config

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.family.util.SharedPreferencesUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application() {

    val SP_NAME = "fcm_message"

    companion object {
        // JWT Token Header 키 값
        const val X_AUTH_TOKEN = "X-AUTH-TOKEN"
        const val SHARED_PREFERENCES_NAME = "DoDamDoDam"
        const val COOKIES_KEY_NAME = "cookies"
        const val AUTO_LOGIN = "auto_login_flag"
        const val JWT = "JWT"

        lateinit var sSharedPreferences: SharedPreferencesUtil
        lateinit var sContentResolver: ContentResolver

        //badge
        lateinit var livePush: MutableLiveData<Int>
        lateinit var isChatting:MutableLiveData<Boolean>
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "a1f1fa77f90e70af62243eda95593018")

        sSharedPreferences = SharedPreferencesUtil(applicationContext)
        sContentResolver = contentResolver

        //badge
        livePush= MutableLiveData(readSharedPreference("fcm").size)
        isChatting=MutableLiveData(false)
    }

    private fun readSharedPreference(key:String): ArrayList<String>{
        val sp = getSharedPreferences(SP_NAME, MODE_PRIVATE)
        val gson = Gson()
        val json = sp.getString(key, "") ?: ""
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val obj: ArrayList<String> = gson.fromJson(json, type) ?: ArrayList()
        return obj
    }
}