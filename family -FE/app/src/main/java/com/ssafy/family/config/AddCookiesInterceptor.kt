package com.ssafy.family.config

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddCookiesInterceptor : Interceptor{

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        // cookie 가져오기
//        val token = sSharedPreferences.getString(ApplicationClass.JWT)
//        token?.let {
//            builder.addHeader(ApplicationClass.X_AUTH_TOKEN, it)
//        }

//        for (cookie in getCookies!!) {
//            builder.addHeader(ApplicationClass.X_AUTH_TOKEN, cookie)
//            Log.d(TAG,"Adding Header: ${cookie}")
//        }
        return chain.proceed(builder.build())
    }
}