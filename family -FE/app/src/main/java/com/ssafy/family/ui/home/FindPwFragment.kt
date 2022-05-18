package com.ssafy.family.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.databinding.FragmentFindPwBinding
import com.ssafy.family.util.InputValidUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPwFragment : Fragment() {

    private lateinit var binding: FragmentFindPwBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFindPwBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        binding.findPWTextViewShowUserID.text = loginViewModel.findLiveData.value!!.data!!.message.toString()
        binding.findIdPageTop.scheduleTitle.text = "뒤로가기"

        binding.findIdPageTop.topLayout.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.home_frame, LoginFragment()).commit()
        }

        binding.findPWPageInputPWNew.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidPassword(input)) {
                binding.findPWPageInputPWNew.error = null
            }
        }

        binding.findPWPageInputPWConfirmNew.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidPassword(input)) {
                if (binding.findPWPageInputPWNew.text.toString() == input) {
                    binding.findPWPageInputPWConfirmNew.error = null
                } else {
                    binding.findPWPageInputPWConfirmNew.error = getString(R.string.passwordCheckErrorMessage)
                }
            }
        }

        binding.findPWPageFindPWBtn.setOnClickListener {
            val id = binding.findPWTextViewShowUserID.text.toString()
            val password = binding.findPWPageInputPWNew.text.toString()
            val newpassword = binding.findPWPageInputPWConfirmNew.text.toString()
            if (InputValidUtil.isValidPassword(password)) {
                if (password == newpassword) {
                    loginViewModel.newPassword(LoginReq(id, password))
                } else {
                    Toast.makeText(requireActivity(), getString(R.string.passwordCheckErrorMessage), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireActivity(), getString(R.string.passwordErrorMessage), Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.baseResponse.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    parentFragmentManager.beginTransaction().replace(R.id.home_frame, LoginFragment()).commit()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "비밀번호를 바꾸지 못했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    parentFragmentManager.beginTransaction().replace(R.id.home_frame, LoginFragment()).commit()
                }
            }
        }
    }

    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }

}