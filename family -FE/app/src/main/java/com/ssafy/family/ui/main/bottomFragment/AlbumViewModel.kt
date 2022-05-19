package com.ssafy.family.ui.main.bottomFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.data.repository.AlbumRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _allAlbumRequestLiveData = MutableLiveData<Resource<AlbumRes>>()
    val allAlbumRequestLiveData : LiveData<Resource<AlbumRes>>
        get() = _allAlbumRequestLiveData

    private val _searchAlbumRequestLiveData = MutableLiveData<Resource<AlbumRes>>()
    val searchAlbumRequestLiveData : LiveData<Resource<AlbumRes>>
        get() = _searchAlbumRequestLiveData

    private val _searchDateAlbumRequestLiveData = MutableLiveData<Resource<AlbumRes>>()
    val searchDateAlbumRequestLiveData : LiveData<Resource<AlbumRes>>
        get() = _searchDateAlbumRequestLiveData

    fun findAllAlbum() = viewModelScope.launch {
        _allAlbumRequestLiveData.postValue(Resource.loading(null))
        _allAlbumRequestLiveData.postValue(albumRepository.findAllAlbum())
    }

    fun searchAlbum(keyword:String) = viewModelScope.launch {
        _searchAlbumRequestLiveData.postValue(Resource.loading(null))
        _searchAlbumRequestLiveData.postValue(albumRepository.searchAlbum(keyword))
    }

    fun searchDateAlbum(date:String) = viewModelScope.launch {
        _searchDateAlbumRequestLiveData.postValue(Resource.loading(null))
        _searchDateAlbumRequestLiveData.postValue(albumRepository.seachDateAlbum(date))
    }

}