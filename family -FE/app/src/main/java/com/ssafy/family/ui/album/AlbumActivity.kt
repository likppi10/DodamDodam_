package com.ssafy.family.ui.album

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.AddAlbumReq
import com.ssafy.family.data.remote.req.UpdateAlbumReq
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.databinding.ActivityAlbumBinding
import com.ssafy.family.ui.main.bottomFragment.AlbumFragment
import com.ssafy.family.util.ErrUtil
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumBinding
    private val detailAlbumViewModel by viewModels<DetailAlbumViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("dddddd", "onCreate: " + LoginUtil.getUserInfo())
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<AllAlbum>(AlbumFragment.FRIEND_INFO)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (data != null) {
            intent.getParcelableExtra<AllAlbum>(AlbumFragment.FRIEND_INFO)?.let {
                detailAlbumViewModel.setSaveAlbum(it)
                Log.d("dddddd", "onCreate: " + detailAlbumViewModel.saveAlbumLiveData.value)
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.album_frame, DetailAlbumFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.album_frame, SelectPhotoFragment())
                .commit()
        }
        detailAlbumViewModel.titleLiveData.observe(this) {
            binding.albumTopInclude.scheduleTitle.text = it
        }
        binding.albumTopInclude.topLayout.setOnClickListener {
            finish()
        }
        detailAlbumViewModel.bottombuttonLeftLivedate.observe(this) {
            if (it == "") {
                binding.albumButtonInclude.button.visibility = View.GONE
            } else {
                binding.albumButtonInclude.button.visibility = View.VISIBLE
                binding.albumButtonInclude.button.text = it
            }
            if(it=="취소"){
                binding.albumButtonInclude.button.setOnClickListener {
                    finish()
                }
            }else if(it == "돌아가기"){
                binding.albumButtonInclude.button.setOnClickListener {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.album_frame, SelectPhotoFragment())
                        .commit()
                }
            }

        }
        detailAlbumViewModel.bottombuttonRightLivedate.observe(this) {
            if (it == "") {
                binding.albumButtonInclude.button2.visibility = View.GONE
            } else {
                binding.albumButtonInclude.button2.visibility = View.VISIBLE
                binding.albumButtonInclude.button2.text = it
            }
            if (it == "" && detailAlbumViewModel.bottombuttonLeftLivedate.value == "") {
                binding.albumButtonInclude.root.visibility = View.GONE
            } else {
                binding.albumButtonInclude.root.visibility = View.VISIBLE
            }

            if (it == "완료") {
                binding.albumButtonInclude.button2.setOnClickListener {
                    if(detailAlbumViewModel.isUpdate){
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.album_frame, UpdateAlbumFragment())
                            .commit()
                    }else{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.album_frame, AddAlbumFragment())
                            .commit()
                    }


                }
            }
            if (it == "등록") {
                binding.albumButtonInclude.button2.setOnClickListener {
                    Log.d("ddddd", "등록: ")
                    val hashTag = detailAlbumViewModel.hashTag
                    val date = detailAlbumViewModel.date
                    val mainIndex = detailAlbumViewModel.mainIndex
                    Log.d("ddddd", "onCreate: " + hashTag)
                    Log.d("ddddd", "onCreate: " + date)
                    Log.d("ddddd", "onCreate: " + mainIndex)
                    if (!hashTag.isNullOrEmpty() && !date.isNullOrEmpty() && mainIndex != null) {
                        Log.d("ddddd", "in: ")
                        detailAlbumViewModel.addAlbum(
                            AddAlbumReq(
                                hashTag,
                                date,
                                mainIndex
                            )
                        )
                        detailAlbumViewModel.hashTag = arrayListOf()
                        detailAlbumViewModel.date = ""
                        detailAlbumViewModel.mainIndex = 0
                    }
                }
            }
            if (it == "수정") {
                binding.albumButtonInclude.button2.setOnClickListener {
                    Log.d("ddddd", "수정: ")
                    val hashTag = detailAlbumViewModel.hashTag
                    val date = detailAlbumViewModel.date
                    val mainIndex = detailAlbumViewModel.mainIndex
                    val pictureList = detailAlbumViewModel.pictureIdList
                    Log.d("ddddd", "onCreate: " + hashTag)
                    Log.d("ddddd", "onCreate: " + date)
                    Log.d("ddddd", "onCreate: " + mainIndex)
                    if (!hashTag.isNullOrEmpty() && !date.isNullOrEmpty() && mainIndex != null) {
                        Log.d("ddddd", "in: ")
                        detailAlbumViewModel.updateAlbum(
                            UpdateAlbumReq(
                                hashTag,
                                date,
                                mainIndex,
                                pictureList
                            ),
                            detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture.albumId
                        )
                        detailAlbumViewModel.hashTag = arrayListOf()
                        detailAlbumViewModel.date = ""
                        detailAlbumViewModel.mainIndex = 0
                    }
                }
            }
        }
        detailAlbumViewModel.addAlbumRequestLiveData.observe(this){
            when(it.status){
                Status.SUCCESS->{
                    Log.d("ddddd", "SUCCESS: ")
                    dismissLoading()
                }
                Status.ERROR->{
                    Log.d("ddddd", "ERROR: ")
                    dismissLoading()
                }
                Status.LOADING->{
                    Log.d("ddddd", "LOADING: ")
                    setLoading()
                }
            }
        }
       detailAlbumViewModel.updateAlbumRequestLiveData.observe(this){
           when(it.status){
               Status.SUCCESS -> {
                   finish()
                   dismissLoading()
               }
               Status.ERROR -> {
                   Toast.makeText(this, ErrUtil.setErrorMsg(it.message), Toast.LENGTH_SHORT)
                       .show()
                   dismissLoading()
               }
               Status.LOADING -> {
                   setLoading()
               }
               Status.EXPIRED -> {
                   dismissLoading()
               }
           }
       }
        detailAlbumViewModel.addAlbumRequestLiveData.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    finish()
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(this, ErrUtil.setErrorMsg(it.message), Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                }
            }
        }
    }
    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }
}