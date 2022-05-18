package com.ssafy.family.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.family.databinding.FragmentSearchDateBinding
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.main.bottomFragment.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SearchDateFragment : Fragment() {

    private lateinit var binding: FragmentSearchDateBinding
    private val albumViewModel by activityViewModels<AlbumViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        val now = System.currentTimeMillis()
        val date = Date(now)
        val yearformat = SimpleDateFormat("yyyy")
        val monthformat = SimpleDateFormat("MM")
        binding.searchEditYear.hint = yearformat.format(date)
        binding.searchEditMonth.hint = monthformat.format(date)
        binding.searchButton.setOnClickListener {
            val year = binding.searchEditYear.text.toString()
            val month = binding.searchEditMonth.text.toString()
            if (year.length == 4) {
                if (month.length == 2) {
                    albumViewModel.searchDateAlbum(year + month)
                } else {
                    binding.searchEditMonth.error = "2자리의 월을 입력해주세요"
                }
            } else {
                binding.searchEditYear.error = "4자리의 연도를 입력해주세요"
            }
        }
    }

}