package com.ssafy.family.ui.album

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.data.remote.res.AlbumPicture
import com.ssafy.family.data.remote.res.HashTag
import com.ssafy.family.databinding.FragmentAddAlbumBinding
import com.ssafy.family.ui.Adapter.AlbumTagAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumPhotoAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddAlbumFragment : Fragment() {

    private lateinit var binding: FragmentAddAlbumBinding
    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()

    private lateinit var photoAdapter: DetailAlbumPhotoAdapter
    private lateinit var tagAdapter: AlbumTagAdapter
    private lateinit var addtag: String

    private val tagItemClickListener = object :AlbumTagAdapter.ItemClickListener{
        override fun onClick(item: HashTag) {
            detailAlbumViewModel.hashTag.remove(item)
            tagAdapter.datas = detailAlbumViewModel.hashTag
            tagAdapter.notifyDataSetChanged()
        }
    }

    private val photoClickListener = object : DetailAlbumPhotoAdapter.ItemClickListener {
        override fun onClick(item: AlbumPicture) {
            photoAdapter.datas.forEach { it.main = false }
            detailAlbumViewModel.mainIndex = photoAdapter.datas.indexOf(item)
            photoAdapter.notifyDataSetChanged()
            item.main = true
            photoAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        picker()
    }

    private fun picker() {
        val year = binding.addAlbumDatePicker.year
        val month = binding.addAlbumDatePicker.month
        val day = binding.addAlbumDatePicker.dayOfMonth
        val datePicker = binding.addAlbumDatePicker
        binding.addAlbumTimeText.text = "날짜 : ${year}년 ${month+1}월 ${day}일"
        detailAlbumViewModel.date = "${year}-${month+1}-${day}"
        datePicker.init(
            year, month, day
        ) { view, year, monthOfYear, dayOfMonth ->
            binding.addAlbumTimeText.text = "날짜 : ${year}년 ${monthOfYear+1}월 ${dayOfMonth}일"
            detailAlbumViewModel.date = "${year}-${monthOfYear+1}-${dayOfMonth}"
        }
    }

    private fun initView() {
        tagAdapter = AlbumTagAdapter(requireActivity()).apply {
            itemClickListener = this@AddAlbumFragment.tagItemClickListener
        }
        binding.addAlbumTagRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tagAdapter
        }
        detailAlbumViewModel.setTitle("앨범 등록")
        detailAlbumViewModel.setBottomButton("돌아가기", "등록")
        photoAdapter = DetailAlbumPhotoAdapter(requireActivity(), 1).apply {
            itemClickListener = this@AddAlbumFragment.photoClickListener
        }
        binding.addAlbumPhotoRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }

        val uriList = detailAlbumViewModel.selectedImgUriList
        val albumpictureList = arrayListOf<AlbumPicture>()
        uriList.forEach { albumpictureList.add(AlbumPicture(getAbsolutePath(it) ?: "", false,0)) }
        photoAdapter.datas = albumpictureList
        photoAdapter.notifyDataSetChanged()

        val tagList = arrayListOf<HashTag>()
        tagAdapter.datas = tagList
        binding.addAlbumTagEditButton.setOnClickListener {
            val regex = Regex("^[ㄱ-ㅎ가-힣A-Za-z0-9\\s]*$")
            val tagtext = binding.addAlbumTagEditText.editText?.text.toString()
            if (tagtext.length<15) {
                if (tagtext.matches(regex)) {
                    if(tagtext.length == 0){
                        Toast.makeText(requireActivity(), "태그를 입력해주세요!", Toast.LENGTH_SHORT).show()
                    }else{
                        addtag = "#" + tagtext
                        if (tagList.size < 3) {
                            tagList.add(HashTag(addtag))
                            detailAlbumViewModel.hashTag = tagList
                            tagAdapter.datas = detailAlbumViewModel.hashTag
                            tagAdapter.notifyDataSetChanged()
                            binding.addAlbumTagEditText.editText?.setText("")
                        } else {
                            Toast.makeText(requireActivity(), "태그는 최대 3개까지에요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }  else {
                    Toast.makeText(requireActivity(), "특수문자는 사용불가해요", Toast.LENGTH_SHORT).show()
                }
            } else if(tagtext.length>15) {
                Toast.makeText(requireActivity(), "태그는 15자 미만으로 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "태그를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        val fileList = arrayListOf<File>()
        uriList.forEach { getRealFile(it)?.let { it1 -> fileList.add(it1) } }
        detailAlbumViewModel.files = fileList
        val pathList = arrayListOf<String>()
        uriList.forEach { getAbsolutePath(it)?.let { it1 -> pathList.add(it1) } }
        detailAlbumViewModel.paths = pathList
    }

    private fun getRealFile(uri: Uri): File? {
        var uri: Uri? = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        var cursor: Cursor? = uri?.let {
            context?.contentResolver?.query(
                it,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc"
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

    private fun getAbsolutePath(imgUri: Uri): String? {
        val cursor = context?.contentResolver?.query(
            imgUri,
            arrayOf(MediaStore.MediaColumns.DATA),
            null,
            null,
            null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }
        return null
    }

}