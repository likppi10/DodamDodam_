package com.ssafy.family.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.databinding.FragmentLoginBinding
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.ui.main.MainActivity.Companion.channel_id
import com.ssafy.family.ui.startsetting.StartSettingActivity
import com.ssafy.family.util.InputValidUtil
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import com.ssafy.family.util.UiMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.loginPageLoginBtn.setOnClickListener {
            if (isValidForm()) {
                login()
            }
        }
        binding.loginPageInputID.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidId(input)) {
                dismissErrorOnId()
            }
        }
        binding.loginPageInputPW.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidPassword(input)) {
                dismissErrorOnPassword()
            }
        }
        binding.loginPageFindUserBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.home_frame, FindIdFragment())
                .commit()
        }
        binding.loginPageSignBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.home_frame, SignFragment())
                .commit()
        }
        loginViewModel.loginResult.observe(requireActivity()) {
            when (it) {
                UiMode.SUCCESS -> {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                else -> {
                    Toast.makeText(requireContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        loginViewModel.loginRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    LoginUtil.setAutoLogin(loginViewModel.isAutoLogin)
                    LoginUtil.saveUserInfo(it.data!!.dataSet!!)
                    Log.d("dddd", "initView: " + LoginUtil.getUserInfo())
                    // TODO: 에러나는지 확인 attach
                    dismissLoading()
                    getFCM()
                    startActivity(Intent(requireContext(), StartSettingActivity::class.java))
                    requireActivity().finish()

                }
                Status.EXPIRED->{
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    login()
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }

        }
        loginViewModel.baseResponse.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    startActivity(Intent(requireContext(), StartSettingActivity::class.java))
                    requireActivity().finish()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }
    }

    private fun addFCM(fcmToken: AddFcmReq) {
        loginViewModel.addFCM(fcmToken)
    }

    private fun login() {
        val id = binding.loginPageInputID.text.toString()
        val pw = binding.loginPageInputPW.text.toString()
        loginViewModel.Login(LoginReq(id, pw))
    }

    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }

    private fun isValidForm(): Boolean {
        val id = binding.loginPageInputID.text.toString()
        val pw = binding.loginPageInputPW.text.toString()
        var flag = 1
        // 이메일 유효성 검사
        if (InputValidUtil.isValidId(id)) {
            dismissErrorOnId()
        } else {
            flag *= 0
            setErrorOnId()
        }
        // 비밀번호 유효성 검사
        if (InputValidUtil.isValidPassword(pw)) {
            dismissErrorOnPassword()
        } else {
            flag *= 0
            setErrorOnPassword()
        }
        // 전부 통과해야 flag == 1
        return flag == 1
    }

    private fun setErrorOnId() {
        binding.textInputLayoutLoginFID.error = resources.getString(R.string.idErrorMessage)
    }

    private fun dismissErrorOnId() {
        binding.textInputLayoutLoginFID.error = null
    }

    private fun dismissErrorOnPassword() {
        binding.textInputLayoutLoginFPW.error = null
    }

    private fun setErrorOnPassword() {
        binding.textInputLayoutLoginFPW.error =
            resources.getString(R.string.passwordErrorMessage)
    }
    fun getFCM() {
        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            addFCM(AddFcmReq(task.result!!))
        })
        createNotificationChannel(channel_id, "ssafy")
    }

    // NotificationChannel 설정
    private fun createNotificationChannel(id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)

            val notificationManager = context?.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    // NotificationChannel 설정
}