package com.ssafy.family.di

import com.ssafy.family.BuildConfig
import com.ssafy.family.config.AddCookiesInterceptor
import com.ssafy.family.config.ReceivedCookiesInterceptor
import com.ssafy.family.config.XAccessTokenInterceptor
import com.ssafy.family.data.remote.api.*
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.api.FamilyAPI
import com.ssafy.family.data.remote.api.CalendarAPI
import com.ssafy.family.data.remote.api.AlbumAPI
import com.ssafy.family.data.remote.api.MainEventAPI
import com.ssafy.family.data.remote.api.ChattingAPI
import com.ssafy.family.data.remote.api.MainFamilyAPI
import com.ssafy.family.data.remote.url.Url.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
    // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val TIME_OUT = 100000L
        OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS).setLevel(
                    HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .addInterceptor(AddCookiesInterceptor())  //쿠키 전송
            .addInterceptor(ReceivedCookiesInterceptor()) //쿠키 추출
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAccountApiService(retrofit: Retrofit): AccountAPI {
        return retrofit.create(AccountAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideFamilyApiService(retrofit: Retrofit): FamilyAPI {
        return retrofit.create(FamilyAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCalendarApiService(retrofit: Retrofit): CalendarAPI {
        return retrofit.create(CalendarAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideAlbumApiService(retrofit: Retrofit): AlbumAPI {
        return retrofit.create(AlbumAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideStatusApiService(retrofit: Retrofit): StatusAPI {
        return retrofit.create(StatusAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMainEventApiService(retrofit: Retrofit): MainEventAPI {
        return retrofit.create(MainEventAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideChatApiService(retrofit: Retrofit): ChattingAPI {
        return retrofit.create(ChattingAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMainFamilyApiService(retrofit: Retrofit): MainFamilyAPI {
        return retrofit.create(MainFamilyAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideSettingApiService(retrofit: Retrofit): SettingAPI {
        return retrofit.create(SettingAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideWishtreeApiService(retrofit: Retrofit): WishtreeAPI {
        return retrofit.create(WishtreeAPI::class.java)
    }

}