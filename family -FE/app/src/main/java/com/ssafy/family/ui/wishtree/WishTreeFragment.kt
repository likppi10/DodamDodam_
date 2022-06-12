package com.ssafy.family.ui.wishtree

import android.animation.Animator
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.WishTree
import com.ssafy.family.databinding.FragmentWishTreeBinding
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class WishTreeFragment : Fragment(), WishTreeDialogInterface {

    private lateinit var binding: FragmentWishTreeBinding
    private val wishTreeViewModel by activityViewModels<WishTreeViewModel>()
    private val textViewList: MutableList<TextView> = ArrayList()
    private val imageViewList: MutableList<ImageView> = ArrayList()
    private val deleteBtnList: MutableList<ImageView> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wishTreeViewModel.getWishTree()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWishTreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        // textViewList, imageViewList 채우기
        fillViewList()

        // 소원 등록 버튼 클릭 리스너 등록
        binding.wishTreeAddBtn.setOnClickListener {
            WishTreeDialog(requireContext(), this).show()
        }

        // 로티 에니메이션 리스너 등록
        val lottie = binding.lottieOpenBox
        lottie.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                lottie.visibility = View.GONE
                // 애니메이션 끝나면 dialog 띄우기
                val profileImg = wishTreeViewModel.profileImg
                val role = wishTreeViewModel.role
                val content = wishTreeViewModel.content
                val wishBoxSelectDialog = WIshBoxSelectDialog(requireContext(), profileImg, role, content)
                wishBoxSelectDialog.show()
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationRepeat(p0: Animator?) {}
        })

        // 데이터 들어오는지 확인
        wishTreeViewModel.getWishTreeResLiveData.observe(requireActivity()) {
            if (it.status == Status.SUCCESS) {

                // 글쓰기 여부, 위시리스트, 총 길이 받기
                val myWishPosition: Int = it.data!!.dataSet!!.myWishPosition
                val wishTreeArray: List<WishTree> = it.data.dataSet!!.wishTree
                val dataLength: Int = wishTreeArray.size

                if (dataLength == 0) {
                    binding.wishTreeAddBtn.visibility = View.VISIBLE
                }
                for (i: Int in 0 until dataLength) {

                    val thisPosition = wishTreeArray[i].position

                    // 텍스트 뷰 설정
                    val selectedTextView: TextView = textViewList[thisPosition]
                    selectedTextView.visibility = View.VISIBLE
                    // 해당하는 텍스트뷰에 .text 넣기(role)
                    selectedTextView.text = wishTreeArray[i].role

                    // 이미지 뷰 설정
                    // 1. 내가 쓴 글이 없는 경우 등록 버튼 생성
                    if (myWishPosition == -1) {
                        binding.wishTreeAddBtn.visibility = View.VISIBLE
                    } else {
                        binding.wishTreeAddBtn.visibility = View.GONE
                    }

                    // 2. 내가 쓴 글이면 수정/삭제 아니면 조회 클릭 리스너 등록
                    val selectedImageView: ImageView = imageViewList[thisPosition]
                    if (thisPosition == myWishPosition) { // 내 소원(수정/삭제)
                        // 클릭 시 수정 dialog 띄우기
                        selectedImageView.visibility = View.VISIBLE
                        selectedImageView.setOnClickListener(View.OnClickListener {
                            // dialog data 생성
                            wishTreeViewModel.selectWishBox(i)
                            WishTreeDialog(requireContext(), this, wishTreeViewModel.wishTreeId, wishTreeViewModel.content
                            ).show()
                        })

                        // 삭제 버튼
                        val deletedBtn: ImageView = deleteBtnList[thisPosition]
                        deletedBtn.visibility = View.VISIBLE
                        deletedBtn.setOnClickListener(View.OnClickListener {
                            WishTreeDeleteDialog(requireContext(), this, wishTreeArray[i].wishTreeId
                            ).show()
                        })
                    } else { // 다른 사람 소원(애니메이션 띄우기 -> 끝나면 다이얼로그 나옴)
                        selectedImageView.visibility = View.VISIBLE
                        // 해당하는 이미지뷰에 클릭 리스너 달기 (로티 에니메이션 실행 -> 다이얼로그 띄우기)
                        selectedImageView.setOnClickListener(View.OnClickListener {
                            wishTreeViewModel.selectWishBox(i)
                            // lottie open box 애니메이션
                            lottie.visibility = View.VISIBLE
                            lottie.playAnimation()
                        })
                    }
                }
            } else if (it.status == Status.LOADING) {
                // 로딩이면 일단 아무것도 하지 않음
            } else {
                Toast.makeText(requireContext(), R.string.unknownErrorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        wishTreeViewModel.createWishTreeResLiveData.observe(requireActivity()) {
            if (it.status == Status.SUCCESS) {
                wishTreeViewModel.getWishTree()
            }
        }

        wishTreeViewModel.deleteWishTreeResLiveData.observe(requireActivity()) {
            if (it.status == Status.SUCCESS) {
                wishTreeViewModel.getWishTree()
            }
        }
    }

    // 소원 생성 버튼 클릭(WishTreeCreateDialogInterface implement -> dialog와 공유)
    override fun onCreateBtnClicked(content: String) {
        wishTreeViewModel.createWish(content)
    }

    override fun onUpdateBtnClicked(myWishListId: Int, content: String) {
        wishTreeViewModel.updateWish(myWishListId, content)
    }

    override fun onDeleteBtnClicked(wishTreeId: Int) {
        // UI 제거
        val wishTreeData = wishTreeViewModel.getWishTreeResLiveData.value!!.data!!.dataSet!!.wishTree
        for (i in wishTreeData.indices) {
            if (wishTreeData[i].wishTreeId == wishTreeId) {
                val position = wishTreeData[i].position
                textViewList[position].visibility = View.GONE
                imageViewList[position].visibility = View.GONE
                deleteBtnList[position].visibility = View.GONE
            }
        }
        // Data 제거
        wishTreeViewModel.deleteWish(wishTreeId)
    }

    private fun fillViewList(){
        // TextViewList
        textViewList.add(binding.giftBox0.giftBoxText)
        textViewList.add(binding.giftBox1.giftBoxText)
        textViewList.add(binding.giftBox2.giftBoxText)
        textViewList.add(binding.giftBox3.giftBoxText)
        textViewList.add(binding.giftBox4.giftBoxText)
        textViewList.add(binding.giftBox5.giftBoxText)
        textViewList.add(binding.giftBox6.giftBoxText)
        textViewList.add(binding.giftBox7.giftBoxText)
        // ImageViewList
        imageViewList.add(binding.giftBox0.giftBoxImg)
        imageViewList.add(binding.giftBox1.giftBoxImg)
        imageViewList.add(binding.giftBox2.giftBoxImg)
        imageViewList.add(binding.giftBox3.giftBoxImg)
        imageViewList.add(binding.giftBox4.giftBoxImg)
        imageViewList.add(binding.giftBox5.giftBoxImg)
        imageViewList.add(binding.giftBox6.giftBoxImg)
        imageViewList.add(binding.giftBox7.giftBoxImg)
        // DeleteBtnList
        deleteBtnList.add(binding.giftBox0.wishDeleteBtn)
        deleteBtnList.add(binding.giftBox1.wishDeleteBtn)
        deleteBtnList.add(binding.giftBox2.wishDeleteBtn)
        deleteBtnList.add(binding.giftBox3.wishDeleteBtn)
        deleteBtnList.add(binding.giftBox4.wishDeleteBtn)
        deleteBtnList.add(binding.giftBox5.wishDeleteBtn)
        deleteBtnList.add(binding.giftBox6.wishDeleteBtn)
        deleteBtnList.add(binding.giftBox7.wishDeleteBtn)
    }

}