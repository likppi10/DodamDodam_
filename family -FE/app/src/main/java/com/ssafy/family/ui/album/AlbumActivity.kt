package com.ssafy.family.ui.album

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.AddAlbumReq
import com.ssafy.family.data.remote.req.UpdateAlbumReq
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.databinding.ActivityAlbumBinding
import com.ssafy.family.ui.main.bottomFragment.AlbumFragment
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumBinding
    private val detailAlbumViewModel by viewModels<DetailAlbumViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<AllAlbum>(AlbumFragment.FRIEND_INFO)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        if (data != null) {
            intent.getParcelableExtra<AllAlbum>(AlbumFragment.FRIEND_INFO)?.let {
                detailAlbumViewModel.setSaveAlbum(it)
            }
            supportFragmentManager.beginTransaction().replace(R.id.album_frame, DetailAlbumFragment()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.album_frame, SelectPhotoFragment()).commit()
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
                    supportFragmentManager.beginTransaction().replace(R.id.album_frame, SelectPhotoFragment()).commit()
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
                        if(detailAlbumViewModel.photosSize > 50000000){
                            Toast.makeText(this, "선택하신 사진들의 크기가 너무 큽니다!", Toast.LENGTH_SHORT).show()
                        }else{
                            supportFragmentManager.beginTransaction().replace(R.id.album_frame, UpdateAlbumFragment()).commit()
                        }
                    }else{
                        if(detailAlbumViewModel.photosSize > 50000000){
                            Toast.makeText(this, "선택하신 사진들의 크기가 너무 큽니다!", Toast.LENGTH_SHORT).show()
                        }else{
                            supportFragmentManager.beginTransaction().replace(R.id.album_frame, AddAlbumFragment()).commit()
                        }
                    }
                }
            }

            if (it == "등록") {
                binding.albumButtonInclude.button2.setOnClickListener {
                    val hashTag = detailAlbumViewModel.hashTag
                    val date = detailAlbumViewModel.date
                    val mainIndex = detailAlbumViewModel.mainIndex
                    if (!hashTag.isNullOrEmpty() && !date.isNullOrEmpty() && mainIndex != null) {
                        detailAlbumViewModel.addAlbum(AddAlbumReq(hashTag, date, mainIndex))
                        detailAlbumViewModel.hashTag = arrayListOf()
                        detailAlbumViewModel.date = ""
                        detailAlbumViewModel.mainIndex = 0
                    }else if(detailAlbumViewModel.paths.size==0) {
                        Toast.makeText(this, "돌아가셔서 사진을 골라주세요!", Toast.LENGTH_SHORT).show()
                    }else if(hashTag.isNullOrEmpty()) {
                        Toast.makeText(this, "해시태그를 입력해주세요!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "사진을 너무 많이 골랐어요. 나눠서 다시 시도해주세요!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            if (it == "수정") {
                binding.albumButtonInclude.button2.setOnClickListener {
                    val hashTag = detailAlbumViewModel.hashTag
                    val date = detailAlbumViewModel.date
                    val mainIndex = detailAlbumViewModel.mainIndex
                    val pictureList = detailAlbumViewModel.pictureIdList
                    if (!hashTag.isNullOrEmpty() && !date.isNullOrEmpty() && mainIndex != null) {
                        detailAlbumViewModel.updateAlbum(
                            UpdateAlbumReq(hashTag, date, mainIndex, pictureList),
                            detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture.albumId
                        )
                        detailAlbumViewModel.hashTag = arrayListOf()
                        detailAlbumViewModel.date = ""
                        detailAlbumViewModel.mainIndex = 0
                    }else if(detailAlbumViewModel.paths.size==0) {
                        Toast.makeText(this, "사진을 골라주세요!", Toast.LENGTH_SHORT).show()
                    }else if(hashTag.isNullOrEmpty()) {
                        Toast.makeText(this, "해시태그를 입력해주세요!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "사진을 너무 많이 골랐어요. 나눠서 다시 시도해주세요!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        detailAlbumViewModel.addAlbumRequestLiveData.observe(this){
            when(it.status){
                Status.SUCCESS->{
                    Toast.makeText(this, "앨범 등록이 완료되었어요!", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    finish()
                }
                Status.ERROR->{
                    Toast.makeText(this, "앨범 등록이 실패했어요. 다시 시도해주세요!", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    finish()
                }
                Status.LOADING->{
                    setLoading()
                }
            }
        }

       detailAlbumViewModel.updateAlbumRequestLiveData.observe(this){
           when(it.status){
               Status.SUCCESS -> {
                   Toast.makeText(this, "앨범 수정이 완료되었어요!", Toast.LENGTH_SHORT).show()
                   dismissLoading()
                   finish()
               }
               Status.ERROR -> {
                   Toast.makeText(this, "앨범 수정이 실패했어요. 다시 시도해주세요!", Toast.LENGTH_SHORT).show()
                   dismissLoading()
                   finish()
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