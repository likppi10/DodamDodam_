package com.ssafy.family.di

import com.ssafy.family.data.remote.api.*
import com.ssafy.family.data.repository.*
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.api.FamilyAPI
import com.ssafy.family.data.remote.api.CalendarAPI
import com.ssafy.family.data.remote.api.MainFamilyAPI
import com.ssafy.family.data.remote.api.AlbumAPI
import com.ssafy.family.data.remote.api.MainEventAPI
import com.ssafy.family.data.remote.api.ChattingAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAccountRepository(
        apiAPI: AccountAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): AccountRepository
            = AccountRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideFamilyRepository(
        apiAPI: FamilyAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): FamilyRepository
            = FamilyRepositoryImpl(apiAPI, ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideChatRepository(
        chatAPI: ChattingAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): ChatRepository
            = ChatRepositoryImpl(chatAPI, ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideAlbumRepository(
        albumAPI: AlbumAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): AlbumRepository
            = AlbumRepositoryImpl(albumAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideCalendarRepository(
        apiAPI: CalendarAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): CalendarRepository
            = CalendarRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideStatusRepository(
        apiAPI: StatusAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): StatusRepository
            = StatusRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideMainFamilyRepository(
        mainFamilyAPI: MainFamilyAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): MainFamilyRepository
            = MainFamilyRepositoryImpl(mainFamilyAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideMainEventRepository(
        apiAPI: MainEventAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): MainEventRepository
            = MainEventRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideSettingRepository(
        apiAPI: SettingAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): SettingRepository
            = SettingRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideWishtreeRepository(
        apiAPI: WishtreeAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): WishtreeRepository
            = WishtreeRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

}