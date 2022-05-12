package com.ssafy.family.data.repository

import com.ssafy.family.data.remote.api.WishtreeAPI
import kotlinx.coroutines.CoroutineDispatcher

class WishtreeRepositoryImpl(
    private val api: WishtreeAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
): WishtreeRepository {

}