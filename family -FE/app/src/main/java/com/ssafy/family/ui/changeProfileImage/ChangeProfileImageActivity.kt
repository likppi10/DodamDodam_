package com.ssafy.family.ui.changeProfileImage

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityChangeProfileImageBinding
import com.ssafy.family.ui.Adapter.SinglePhotoRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeProfileImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeProfileImageBinding
    private lateinit var photoRecyclerViewAdapter: SinglePhotoRecyclerViewAdapter

    private var selectedImageUri: Uri? = null
    private var selectedImagePosition: Int = -1
    private var recyclerViewMode: String = "character"

    private val itemClickListener = object: SinglePhotoRecyclerViewAdapter.ItemClickListener {
        override fun onClick(uri: Uri, view: View, position: Int) {
            if (selectedImagePosition == position) { // 사진 같은거 선택 시 선택 취소
                selectedImageUri = null
                selectedImagePosition = -1
                Glide.with(this@ChangeProfileImageActivity)
                    .load(ContextCompat.getDrawable(this@ChangeProfileImageActivity, R.drawable.image_fail))
                    .into(binding.checkPhotoImg)
            } else {
                selectedImageUri = uri
                selectedImagePosition = position
                Glide.with(this@ChangeProfileImageActivity).load(uri).into(binding.checkPhotoImg)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeProfileImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 바 설정
        binding.changeProfileTop.scheduleTitle.text = "프로필 이미지 변경"

        // 뒤로가기 버튼 설정
        binding.changeProfileTop.topLayout.setOnClickListener {
            finish()
        }

        // 리사이클러뷰 설정
        photoRecyclerViewAdapter = SinglePhotoRecyclerViewAdapter().apply {
            itemClickListener = this@ChangeProfileImageActivity.itemClickListener
        }

        binding.selectRecyclerView.apply {
            adapter = photoRecyclerViewAdapter
            layoutManager = GridLayoutManager(this@ChangeProfileImageActivity, 4, RecyclerView.VERTICAL, false)
        }

        // 초기 이미지 받아오기
        val defaultImage = this@ChangeProfileImageActivity.intent.getStringExtra("imageUri")
        if (defaultImage == "null") {
            selectedImageUri = null
            Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.image_fail)).into(binding.checkPhotoImg)
        } else {
            val imageUri = Uri.parse(defaultImage)
            selectedImageUri = imageUri
            Glide.with(this).load(imageUri).into(binding.checkPhotoImg)
        }

        // 탭 설정
        binding.characterTab.setOnClickListener {
            if (recyclerViewMode == "album") {
                // 앨범에서 넘어오면 배경색 글씨색 바꾸기
                binding.characterTab.setBackgroundResource(R.color.beige02)
                binding.characterTab.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.myAlbumTab.setBackgroundResource(R.color.white)
                binding.myAlbumTab.setTextColor(ContextCompat.getColor(this, R.color.black_text))
                // 모드 바꾸고 reload
                recyclerViewMode = "character"
                reloadImages()
            }
        }

        binding.myAlbumTab.setOnClickListener {
            if (recyclerViewMode == "character") {
                binding.myAlbumTab.setBackgroundResource(R.color.beige02)
                binding.myAlbumTab.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.characterTab.setBackgroundResource(R.color.white)
                binding.characterTab.setTextColor(ContextCompat.getColor(this, R.color.black_text))
                recyclerViewMode = "album"
                reloadImages()
            }
        }

        // 하단 선택 버튼 설정
        binding.changeProfileConfirmBtn.setOnClickListener {
            intent.putExtra("imageUri", selectedImageUri.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        reloadImages()
    }

    fun reloadImages() {
        if (recyclerViewMode == "character") {
            photoRecyclerViewAdapter.uris = mutableListOf<Uri>()
            val emojis = resources.getStringArray(R.array.characters)
            for (i in emojis.indices) {
                photoRecyclerViewAdapter.uris.add(Uri.parse(emojis[i]))
            }
            photoRecyclerViewAdapter.notifyDataSetChanged()
        } else {
            photoRecyclerViewAdapter.uris = setImageUrisFromCursor(getPhotoCursor())
            photoRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    // 갤러리 이미지 가져오는 함수 : setImageUrisFromCursor(getPhotoCursor())
    fun setImageUrisFromCursor(cursor: Cursor): MutableList<Uri> {
        val list = mutableListOf<Uri>()
        cursor.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                list.add(uri)
            }
        }
        return list
    }

    fun getPhotoCursor(): Cursor {
        val resolver = contentResolver
        var queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val img = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATA
        )

        val orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        queryUri = queryUri.buildUpon().build()
        return resolver.query(queryUri, img, null, null, orderBy)!!
    }

}