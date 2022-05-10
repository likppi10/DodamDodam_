package com.ssafy.family.ui.status

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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

class SelectFamilyPictureFragment : Fragment() {
    private lateinit var binding: FragmentSelectFamilyPictureBinding
    private lateinit var photoRecyclerViewAdapter: SinglePhotoRecyclerViewAdapter
    private val statusViewModel by activityViewModels<StatusViewModel>()
    var selectedPicturePosition: Int? = null

    private val itemClickListener = object : SinglePhotoRecyclerViewAdapter.ItemClickListener {
        override fun onClick(uri: Uri, view: View, position: Int) {
            if (selectedPicturePosition == position) {
                statusViewModel.setImgUri(null)
                selectedPicturePosition = null
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
    ): View? {
        binding = FragmentSelectFamilyPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.selectFamilyPictureButtonInclude.button.text = "취소"
        binding.selectFamilyPictureButtonInclude.button2.text = "완료"
        photoRecyclerViewAdapter = SinglePhotoRecyclerViewAdapter().apply {
            itemClickListener = this@SelectFamilyPictureFragment.itemClickListener
        }
        binding.recyclerViewAlbumF.apply {
            adapter = photoRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireActivity(), 4, RecyclerView.VERTICAL, false)
        }
        statusViewModel.selectedImgUri.observe(requireActivity()) {
            val imageView = binding.selectedFamilyPicture
            if (it == null) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.image_fail))
            } else {
                Glide.with(imageView).load(it).into(imageView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadImages()
    }

    fun reloadImages() {
        photoRecyclerViewAdapter.uris = setImageUrisFromCursor(getPhotoCursor())
        photoRecyclerViewAdapter.notifyDataSetChanged()
    }

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

}