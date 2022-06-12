package com.ssafy.family.ui.wishtree

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.family.config.BaseResponse
import com.ssafy.family.data.remote.res.WishtreeRes
import com.ssafy.family.data.repository.WishtreeRepository
import com.ssafy.family.util.Resource
import com.ssafy.family.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishTreeViewModel @Inject constructor(private val wishtreeRepository: WishtreeRepository) : ViewModel() {

    private val _getWishTreeResLiveData = MutableLiveData<Resource<WishtreeRes>>()
    val getWishTreeResLiveData: LiveData<Resource<WishtreeRes>>
        get() = _getWishTreeResLiveData

    private val _createWishTreeResLiveData = MutableLiveData<Resource<BaseResponse>>()
    val createWishTreeResLiveData: LiveData<Resource<BaseResponse>>
        get() = _createWishTreeResLiveData

    private val _updateWishTreeResLiveData = MutableLiveData<Resource<BaseResponse>>()
    val updateWishTreeResLiveData: LiveData<Resource<BaseResponse>>
        get() = _updateWishTreeResLiveData

    private val _deleteWishTreeResLiveData = MutableLiveData<Resource<BaseResponse>>()
    val deleteWishTreeResLiveData: LiveData<Resource<BaseResponse>>
        get() = _deleteWishTreeResLiveData

    // dialog 데이터
    var wishTreeId: Int? = null
    var profileImg: String? = null
    var role: String = "소원"
    var content: String = "소원을 적어주세요!"

    fun getWishTree() = viewModelScope.launch{
        _getWishTreeResLiveData.postValue(Resource.loading(null))
        val res = wishtreeRepository.getWishTree()
        _getWishTreeResLiveData.postValue(res)

        // 요청 성공에 내가 등록한 소원이 있으면 기본 데이터 입력
        if (res.status == Status.SUCCESS && res.data!!.dataSet!!.myWishPosition != -1) {
            val myWishPosition = res.data.dataSet!!.myWishPosition
            val wishTree = res.data.dataSet.wishTree
            for (i in wishTree.indices) {
                if (wishTree[i].position == myWishPosition) {
                    wishTreeId = wishTree[i].wishTreeId
                    profileImg = wishTree[i].profileImage
                    role = wishTree[i].role
                    content = wishTree[i].content
                    break
                }
            }
        }

    }

    fun createWish(content: String) = viewModelScope.launch {
        _createWishTreeResLiveData.postValue(Resource.loading(null))
        _createWishTreeResLiveData.postValue(wishtreeRepository.createWishTree(content))
    }

    fun selectWishBox(i: Int) {
        wishTreeId = getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree[i].wishTreeId
        profileImg = getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree[i].profileImage
        role = getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree[i].role
        content = getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree[i].content
    }

    fun updateWish(wishTreeId: Int, content: String) = viewModelScope.launch {
        _updateWishTreeResLiveData.postValue(Resource.loading(null))
        val res = wishtreeRepository.updateWishTree(wishTreeId, content)
        _updateWishTreeResLiveData.postValue(res)
        if (res.status == Status.SUCCESS) {
            getWishTree()
        }
    }

    fun deleteWish(wishTreeId: Int) = viewModelScope.launch {
        _deleteWishTreeResLiveData.postValue(Resource.loading(null))
        val res = wishtreeRepository.deleteWishTree(wishTreeId)
        _deleteWishTreeResLiveData.postValue(res)
        if (res.status == Status.SUCCESS) {
            getWishTree()
        }
    }

}