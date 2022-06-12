package com.ssafy.family.data.repository

import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.req.AddAlbumReq
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.req.UpdateAlbumReq
import com.ssafy.family.data.remote.res.AlbumDetailRes
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.util.Resource
import okhttp3.MultipartBody

interface AlbumRepository {

    suspend fun findAllAlbum(): Resource<AlbumRes>

    suspend fun detailAlbum(albumId:Int):Resource<AlbumDetailRes>

    suspend fun deleteReaction(reactionId: Int):Resource<BaseResponse>

    suspend fun addReaction(albumReactionReq: AlbumReactionReq):Resource<BaseResponse>

    suspend fun searchAlbum(keyword:String): Resource<AlbumRes>

    suspend fun addAlbum(addAlbum:AddAlbumReq, imagefiles: ArrayList<MultipartBody.Part>):Resource<BaseResponse>

    suspend fun seachDateAlbum(date: String): Resource<AlbumRes>

    suspend fun updateAlbum(updateAlbumReq: UpdateAlbumReq,albumId: Int, imagefiles: ArrayList<MultipartBody.Part>):Resource<BaseResponse>

    suspend fun deleteAlbum(albumId: Int):Resource<BaseResponse>

}