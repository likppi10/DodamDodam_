package com.ssafy.family.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.family.databinding.FragmentSearchTagBinding
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.main.bottomFragment.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTagFragment : Fragment() {

    private val albumViewModel by activityViewModels<AlbumViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private lateinit var binding: FragmentSearchTagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchButton.setOnClickListener {
            val keyword = binding.searchEditText.text.toString()
            if(keyword.isNullOrEmpty()||keyword.isNullOrBlank()){
                albumViewModel.findAllAlbum()
            }else{
                albumViewModel.searchAlbum(keyword)
            }
        }
    }

}