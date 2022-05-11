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
import com.ssafy.family.data.remote.req.SignUpReq
import com.ssafy.family.databinding.FragmentSignBinding
import com.ssafy.family.util.ErrUtil
import com.ssafy.family.util.InputValidUtil
import com.ssafy.family.util.Status

class SignFragment : Fragment() {
    lateinit var binding: FragmentSignBinding
    private var checkId = false
    private val loginViewModel by activityViewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.signPageTop.scheduleTitle.text = "뒤로가기"
        binding.signPageTop.topLayout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_frame, LoginFragment())
                .commit()
        }
        binding.signPageInputID.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidId(input)) {
                binding.signPageInputID.error = null
            }
        }
        binding.signPageInputUsername.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidName(input)) {
                binding.signPageInputUsername.error = null
            }
        }
        binding.signPageInputPW.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidPassword(input)) {
                binding.signPageInputPW.error = null
            }
        }
        binding.signPageInputPWConfirm.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidPassword(input)) {
                if (binding.signPageInputPW.text.toString() == input) {
                    binding.signPageInputPW.error = null
                } else {
                    binding.signPageInputPW.error =
                        getString(R.string.passwordCheckErrorMessage)
                }
            }
        }
        binding.signPageCheckIDBtn.setOnClickListener {
            val id = binding.signPageInputID.text.toString()
            if (InputValidUtil.isValidId(id)) {
                loginViewModel.idCheck(id)
            }
        }
        loginViewModel.idCheckLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.sameIdSuccesMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                    checkId = true
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireActivity(),ErrUtil.setErrorMsg(it.message), Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.signPageSignBtn.setOnClickListener {
            val id = binding.signPageInputID.text.toString()
            val password = binding.signPageInputPW.text.toString()
            val confirmpassword = binding.signPageInputPWConfirm.text.toString()
            val name = binding.signPageInputUsername.text.toString()
            if (checkId) {
                if (InputValidUtil.isValidPassword(password)) {
                    if (password == confirmpassword) {
                        if (InputValidUtil.isValidName(name)) {
                            loginViewModel.signUp(SignUpReq(id, password, name))
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.nameErrorMessage),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.passwordCheckErrorMessage),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.passwordErrorMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.emailNotCheckedErrorMessage),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        loginViewModel.signUpLiveData.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    dismissLoading()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.home_frame, LoginFragment())
                        .commit()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireActivity(), ErrUtil.setErrorMsg(it.message), Toast.LENGTH_SHORT).show()
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