package com.ssafy.family.ui.wishtree

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.data.remote.res.WishtreeRes
import com.ssafy.family.data.repository.WishtreeRepository
import com.ssafy.family.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WishTreeViewModel @Inject constructor(private val wishtreeRepository: WishtreeRepository) :
    ViewModel() {
    private val _getWishTreeResLiveData = MutableLiveData<Resource<WishtreeRes>>()
    val getWishTreeResLiveData: LiveData<Resource<WishtreeRes>>
        get() = _getWishTreeResLiveData

    // dialog 데이터
    var profileImg: String? = null
    var role: String = "소원"
    var content: String = "소원을 적어주세요!"

    fun getWishTree() = viewModelScope.launch{
        _getWishTreeResLiveData.postValue(Resource.loading(null))
        _getWishTreeResLiveData.postValue(wishtreeRepository.getWishTree())
    }

    fun selectWishBox(i: Int) {
        profileImg = getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree[i].profileImg
        role = getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree[i].role
        content = getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree[i].content
    }

}