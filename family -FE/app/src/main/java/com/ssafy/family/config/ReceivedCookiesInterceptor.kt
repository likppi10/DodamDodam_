package com.ssafy.family.config

import com.ssafy.family.config.ApplicationClass.Companion.sSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ReceivedCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain):Response{
        val originalResponse: Response = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            for (cookie in originalResponse.headers("Set-Cookie")) {
                val cookieSplit = cookie.split(";")
                val cookieStrings = cookieSplit[0].split("=")
                val cookieKey = cookieStrings[0]
                val cookieValue = cookieStrings[1]
                // 쿠키 key, value로 저장
                sSharedPreferences.setString(cookieKey, cookieValue)
            }
        }
        return originalResponse
    }
}