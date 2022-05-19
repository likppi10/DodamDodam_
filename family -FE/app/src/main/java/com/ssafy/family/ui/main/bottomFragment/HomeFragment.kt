package com.ssafy.family.ui.main.bottomFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.family.ui.Adapter.PagerAdapter
import com.ssafy.family.databinding.FragmentHomeBinding
import com.ssafy.family.ui.main.EventFragment
import com.ssafy.family.ui.main.FamilyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PagerAdapter(requireActivity())
        adapter.addFragment(FamilyFragment(), "Family")
        adapter.addFragment(EventFragment(), "Event")
        binding.viewpager.adapter = adapter
        binding.viewpager.currentItem = 0

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        binding.viewpager.isUserInputEnabled = false
    }

}