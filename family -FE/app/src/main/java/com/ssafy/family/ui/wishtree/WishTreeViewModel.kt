package com.ssafy.family.ui.wishtree

import androidx.lifecycle.ViewModel
import com.ssafy.family.data.repository.WishtreeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WishTreeViewModel @Inject constructor(private val wishtreeRepository: WishtreeRepository) :
    ViewModel() {

}