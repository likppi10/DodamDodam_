package com.ssafy.family.config

import com.ssafy.family.config.ApplicationClass.Companion.X_AUTH_TOKEN
import com.ssafy.family.config.ApplicationClass.Companion.sSharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class XAccessTokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val token: String? = sSharedPreferences.getString(ApplicationClass.JWT)
        token?.let {
            builder.addHeader(X_AUTH_TOKEN, token)
        }
        return chain.proceed(builder.build())
    }
}
