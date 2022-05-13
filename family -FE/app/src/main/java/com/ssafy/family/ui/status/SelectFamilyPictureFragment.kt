package com.ssafy.family.ui.status

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentSelectFamilyPictureBinding
import com.ssafy.family.ui.Adapter.SinglePhotoRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SelectFamilyPictureFragment : Fragment() {
    private lateinit var binding: FragmentSelectFamilyPictureBinding
    private lateinit var photoRecyclerViewAdapter: SinglePhotoRecyclerViewAdapter
    private val statusViewModel by activityViewModels<StatusViewModel>()
    var selectedPicturePosition: Int = -1

    private val itemClickListener = object : SinglePhotoRecyclerViewAdapter.ItemClickListener {
        override fun onClick(uri: Uri, view: View, position: Int) {
            if (selectedPicturePosition == position) {
                statusViewModel.deleteImgUri()
                selectedPicturePosition = -1
            } else {
                statusViewModel.setImgUri(uri)
                selectedPicturePosition = position
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectFamilyPictureBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 하단 버튼 텍스트 설정
        binding.selectFamilyPictureButtonInclude.button.text = "취소"
        binding.selectFamilyPictureButtonInclude.button2.text = "완료"
        // 하단 버튼 클릭리스너 등록
        binding.selectFamilyPictureButtonInclude.button.setOnClickListener(View.OnClickListener {
            requireActivity().finish()
        })
        binding.selectFamilyPictureButtonInclude.button2.setOnClickListener(View.OnClickListener {
            // 사진 변경 api 요청
            val imageFile = statusViewModel.selectedImgUri.value
            statusViewModel.editFamilyPicture(imageUriToFile(imageFile))
            requireActivity().finish()
        })
        // 리사이클러뷰 설정
        photoRecyclerViewAdapter = SinglePhotoRecyclerViewAdapter().apply {
            itemClickListener = this@SelectFamilyPictureFragment.itemClickListener
        }
        binding.recyclerViewAlbumF.apply {
            adapter = photoRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireActivity(), 4, RecyclerView.VERTICAL, false)
        }
        // 선택된 이미지 Uri에 따른 상단 이미지뷰 UI 설정
        statusViewModel.selectedImgUri.observe(requireActivity()) {
            val imageView = binding.selectedFamilyPicture
            if (it == null) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.image_fail))
            } else {
                Glide.with(imageView).load(it).into(imageView)
            }
        }
        statusViewModel.selectedImgUri.observe(requireActivity()) {
            val imageView = binding.selectedFamilyPicture
            if (it != null){
                Glide.with(imageView).load(it).into(imageView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadImages()

        if(requireActivity().intent.getStringExtra("to") == "change"){
            statusViewModel.getFamilyPicture()
        }
    }

    fun reloadImages() {
        photoRecyclerViewAdapter.uris = setImageUrisFromCursor(getPhotoCursor())
        photoRecyclerViewAdapter.notifyDataSetChanged()
    }

    // 갤러리 이미지 가져오는 함수 : setImageUrisFromCursor(getPhotoCursor())
    fun setImageUrisFromCursor(cursor: Cursor): List<Uri> {
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
        val resolver = requireActivity().contentResolver
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

    // 이미지 Uri -> File
    private fun imageUriToFile(uri: Uri?): File? {
        var uri: Uri? = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        var cursor: Cursor? = (activity as StatusActivity).contentResolver.query(
            uri!!,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        )
        if (cursor == null || cursor.columnCount < 1) {
            return null
        }
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path: String = cursor.getString(column_index)
        if (cursor != null) {
            cursor.close()
            cursor = null
        }
        return File(path)
    }

}