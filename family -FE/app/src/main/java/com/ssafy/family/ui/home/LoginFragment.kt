package com.ssafy.family.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.databinding.FragmentLoginBinding
import com.ssafy.family.util.*
import com.ssafy.family.util.LoginUtil.setScocialToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("TAG", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        binding.kakaoIcon.setOnClickListener {
            kakaoLogin()
        }

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
            parentFragmentManager.beginTransaction().replace(R.id.home_frame, FindIdFragment()).commit()
        }

        binding.loginPageSignBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.home_frame, SignFragment()).commit()
        }

        loginViewModel.loginRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    LoginUtil.setAutoLogin(loginViewModel.isAutoLogin)
                    LoginUtil.saveUserInfo(it.data!!.dataSet!!)
                    dismissLoading()
                    getFCM()
                }
                Status.EXPIRED->{
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    login()
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "로그인에 실패했어요. 아이디, 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }

        loginViewModel.socialLoginLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    LoginUtil.setAutoLogin(loginViewModel.isAutoLogin)
                    LoginUtil.saveUserInfo(it.data!!.dataSet!!)
                    dismissLoading()
                    getFCM()
                }
                Status.EXPIRED->{
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    kakaoLogin()
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "회원가입을 완료하지 못했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
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
        binding.textInputLayoutLoginFPW.error = resources.getString(R.string.passwordErrorMessage)
    }

    fun getFCM() {
        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            addFCM(AddFcmReq(task.result!!))
        })
    }

    fun kakaoLogin(){
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    setScocialToken(token.accessToken)
                    loginViewModel.socialLogin()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }

}