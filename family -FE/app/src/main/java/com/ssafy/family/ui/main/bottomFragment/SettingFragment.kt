package com.ssafy.family.ui.main.bottomFragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ssafy.family.R
import com.ssafy.family.config.ApplicationClass.Companion.livePush
import com.ssafy.family.databinding.FragmentSettingBinding
import com.ssafy.family.ui.home.HomeActivity
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.startsetting.StartSettingActivity
import com.ssafy.family.ui.status.StatusActivity
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.LoginUtil.signOut
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val settingViewModel by activityViewModels<SettingViewModel>()

    val SP_NAME = "fcm_message"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        settingViewModel.getStatus()
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
                    Toast.makeText(requireActivity(), "가족코드를 불러오지 못했어요.", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(requireActivity(), "프로필 이미지를 불러오지 못했어요.", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        settingViewModel.exitFamilyRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    signOut()
                    logout()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "가족을 떠나지 못했어요.", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        settingViewModel.getStatusRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(it.data!!.data != null){
                        Glide.with(binding.myStatusImage).load(it.data.data!!.emotion).into(binding.myStatusImage)
                        binding.myStatus.text = it.data.data!!.comment
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "내 상태를 불러오지 못했어요.", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        settingViewModel.logoutRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    signOut()
                    logout()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.copyImageButton.setOnClickListener {
            val clipboard = requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", binding.familyCodeText.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "클립보드에 복사되었어요.", Toast.LENGTH_SHORT).show()
        }

        binding.shareImageButton.setOnClickListener {
            try {
                val sendText = "[도담도담 : DodamDodam]의 초대 방 코드 : ${binding.familyCodeText.text}"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText)
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "가족 코드 공유"))
            } catch (ignored: ActivityNotFoundException) { }
        }

        binding.statusViewBox.setOnClickListener {
            val intent = Intent(requireContext(), StatusActivity::class.java)
            intent.putExtra("to", "edit")
            startActivity(intent)
        }

        binding.myInfoEditButtonBox.setOnClickListener {
            val intent = Intent(requireContext(), StartSettingActivity::class.java)
            intent.putExtra("to", "edit")
            startActivity(intent)
        }

        binding.myStatusEditButtonBox.setOnClickListener {
            val intent = Intent(requireContext(), StatusActivity::class.java)
            intent.putExtra("to", "change")
            startActivity(intent)
        }

        binding.exitGroupButtonBox.setOnClickListener {
            showExitDialog()
        }

        binding.logoutButtonBox.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showExitDialog(){
        var dialog = ExitDialog(requireContext())
        dialog.showDialog()
        dialog.setOnClickListener(object : ExitDialog.OnDialogClickListener {
            override fun onClicked() {
                settingViewModel.exitFamily()
            }
            override fun onClosed() {}
        })
    }

    private fun showLogoutDialog(){
        var dialog = LogoutDialog(requireContext())
        dialog.showDialog()
        dialog.setOnClickListener(object : LogoutDialog.OnDialogClickListener {
            override fun onClicked() {
                settingViewModel.logout()
            }
            override fun onClosed() {}
        })
    }

    private fun writeSharedPreference(key:String, value:ArrayList<String>){
        val sp = requireContext().getSharedPreferences(SP_NAME, AppCompatActivity.MODE_PRIVATE)
        val editor = sp.edit()
        val gson = Gson()
        val json: String = gson.toJson(value)
        editor.putString(key, json)
        editor.apply()
    }

    fun logout() {


        Log.d("로그아웃 했설", "logout: 로그아웃 준비")
        if(isAdded){
            livePush = MutableLiveData(0)
            writeSharedPreference("fcm", arrayListOf())
            Toast.makeText(requireContext(), "로그아웃 했어요.", Toast.LENGTH_SHORT).show()
            Log.d("로그아웃 했설", "logout: 액티비티에서 붙은채로로")
            val intent = Intent(requireContext(), HomeActivity::class.java)
            requireActivity().finishAffinity()
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

}