package com.ssafy.family.ui.main.bottomFragment

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentSettingBinding
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.util.CalendarUtil
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.LoginUtil.deleteFamily
import com.ssafy.family.util.LoginUtil.signOut
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val settingViewModel by activityViewModels<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        settingViewModel.getFamilyCode()
        settingViewModel.getProfileImage()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewModel.getFamilyCodeRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    binding.familyCodeText.text = it.data!!.data!!.code
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    settingViewModel.getFamilyCode()
                }
            }
        }

        settingViewModel.getProfileImageRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(it.data!!.profileImage == null){
                        Glide.with(binding.profileImage).load(R.drawable.image_fail).into(binding.profileImage)
                    }else{
                        Glide.with(binding.profileImage).load(it.data!!.profileImage).into(binding.profileImage)
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    settingViewModel.getProfileImage()
                }
            }
        }

        settingViewModel.exitFamilyRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    deleteFamily()
                    (activity as MainActivity).logout()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    settingViewModel.exitFamily()
                }
            }
        }

        binding.copyImageButton.setOnClickListener {
            val clipboard = requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", binding.familyCodeText.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.shareImageButton.setOnClickListener {
            try {
                val sendText = "Rolling Pictures 초대 방 코드 : ${binding.familyCodeText}"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText)
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "그룹 코드 공유"))
            } catch (ignored: ActivityNotFoundException) { }
        }

        binding.exitGroupButton.setOnClickListener {
            settingViewModel.exitFamily()
        }

        binding.logoutButton.setOnClickListener {
            signOut()
            (activity as MainActivity).logout()
        }
    }

}