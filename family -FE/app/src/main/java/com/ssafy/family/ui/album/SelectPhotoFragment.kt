package com.ssafy.family.ui.album

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.databinding.FragmentSelectPhotoBinding
import com.ssafy.family.ui.Adapter.PhotoPickAdapter
import com.ssafy.family.ui.Adapter.PhotoRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
@SuppressLint("ResourceAsColor")
class SelectPhotoFragment : Fragment() {

    private lateinit var binding: FragmentSelectPhotoBinding
    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()

    var map = hashMapOf<Uri, Boolean>()
    private lateinit var photoRecyclerViewAdapter: PhotoRecyclerViewAdapter
    private lateinit var pickAdapter: PhotoPickAdapter

    private val itemClickListener = object : PhotoRecyclerViewAdapter.ItemClickListener {
        override fun onClick(uri: Uri, view: View, position: Int) {
            if (map[uri]==true) {
                map[uri] = false
                detailAlbumViewModel.deleteImgUri(uri)
                pickAdapter.uris = detailAlbumViewModel.selectedImgUriList
                pickAdapter.notifyDataSetChanged()
                detailAlbumViewModel.photosSize -= 1
                binding.albumSizeText.text = "${detailAlbumViewModel.photosSize}장 / "
            } else {
                if(getRealFile(uri)!!.length() > 5000000){
                    Toast.makeText(requireContext(), "이 사진은 용량이 너무 커요!", Toast.LENGTH_SHORT).show()
                }else if(detailAlbumViewModel.photosSize==10){
                    Toast.makeText(requireContext(), "10장을 전부 골랐어요!", Toast.LENGTH_SHORT).show()
                }else{
                    map[uri] = true
                    detailAlbumViewModel.setSelectedImgUri(uri)
                    pickAdapter.uris = detailAlbumViewModel.selectedImgUriList
                    pickAdapter.notifyDataSetChanged()
                    detailAlbumViewModel.photosSize += 1
                    binding.albumSizeText.text = "${detailAlbumViewModel.photosSize}장 / "
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSelectPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        detailAlbumViewModel.setTitle("사진 선택")
        detailAlbumViewModel.setBottomButton("취소", "완료")

        photoRecyclerViewAdapter = PhotoRecyclerViewAdapter().apply {
            itemClickListener = this@SelectPhotoFragment.itemClickListener
        }

        binding.recyclerViewAlbumF.apply {
            adapter = photoRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireActivity(), 4, RecyclerView.VERTICAL, false)
        }

        pickAdapter = PhotoPickAdapter()

        binding.recyclerViewPick.apply {
            adapter = pickAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.albumSizeText.text = "${detailAlbumViewModel.photosSize}장 / "
    }

    override fun onResume() {
        super.onResume()
        reloadImages()
    }

    fun reloadImages() {
        photoRecyclerViewAdapter.uris = setImageUrisFromCursor(getPhotoCursor())
        photoRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun setImageUrisFromCursor(cursor: Cursor): MutableList<Uri> {
        val list = mutableListOf<Uri>()
        cursor.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString()
                )
                map[uri] = false
                list.add(uri)
            }
        }
        dismissLoading()
        return list
    }

    fun getPhotoCursor(): Cursor {
        setLoading()
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

    private fun getRealFile(uri: Uri): File? {
        var uri: Uri? = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        var cursor: Cursor? = uri?.let {
            context?.contentResolver?.query(it, projection, null,
                null, MediaStore.Images.Media.DATE_MODIFIED + " desc"
            )
        }
        if (cursor == null || cursor.columnCount < 1) {
            return null
        }
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        if (cursor != null) {
            cursor.close()
            cursor = null
        }
        return File(path)
    }

    private fun setLoading() {
        binding.progressBarLoading.visibility = View.VISIBLE
    }
    private fun dismissLoading() {
        binding.progressBarLoading.visibility = View.GONE
    }

}