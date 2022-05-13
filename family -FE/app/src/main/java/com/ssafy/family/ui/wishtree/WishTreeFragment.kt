package com.ssafy.family.ui.wishtree

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.WishTree
import com.ssafy.family.databinding.FragmentWishTreeBinding
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class WishTreeFragment : Fragment() {

    private lateinit var binding: FragmentWishTreeBinding
    private val wishtreeViewModel by activityViewModels<WishTreeViewModel>()
    private val textViewList: MutableList<TextView> = ArrayList()
    private val imageViewList: MutableList<ImageView> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wishtreeViewModel.getWishTree()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishTreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // textViewList, imageViewList 채우기
        fillViewList()
        // 소원 등록 버튼 클릭 리스너 등록
        binding.wishTreeAddBtn.setOnClickListener {

        }
        // 데이터 들어오는지 확인
        wishtreeViewModel.getWishTreeResLiveData.observe(requireActivity()) {
            if (it.status == Status.SUCCESS) {
                // 글쓰기 여부, 위시리스트, 총 길이 받기
                val canCreate: Boolean = it.data!!.dataSet!!.canCreate
                val wishTreeArray: List<WishTree> = it.data.dataSet!!.wishTree
                val dataLength: Int = wishTreeArray.size

                // 총길이만큼 돌면서 위시리스트 데이터 내에서 position에 해당하는 view visible 전환
                if (dataLength > 0) {
                    for (i: Int in 0..dataLength) {
                        val selectedTextView: TextView = textViewList[wishTreeArray[i].position.toInt()]
                        val selectedImageView: ImageView = imageViewList[wishTreeArray[i].position.toInt()]
                        selectedTextView.visibility = View.VISIBLE
                        selectedImageView.visibility = View.VISIBLE
                        // 해당하는 텍스트뷰에 .text 넣기(role)
                        selectedTextView.text = wishTreeArray[i].role
                        // 해당하는 이미지뷰에 클릭 리스너 달기
                        selectedImageView.setOnClickListener(View.OnClickListener {
                            // dialog data 생성
                            wishtreeViewModel.selectWishBox(i)
                            // dialog 띄우기
                            val wishBoxSelectDialog = WIshBoxSelectDialog(requireContext())
                            wishBoxSelectDialog.show()
                            // lottie open box 애니메이션 실행
//                            binding.lottieOpenBox.visibility = View.VISIBLE
//                            val animator = ValueAnimator.ofFloat(0f, 0.71f).setDuration(1000)
//                            animator.addUpdateListener {
//                                binding.lottieOpenBox.setProgress(
//                                    it.getAnimatedValue() as Float
//                                )
//                            }
//                            animator.start()
                        })
                }
                    // canCreate가 true면 글쓰기 버튼 visible 전환
                    if (canCreate) {
                        binding.wishTreeAddBtn.visibility = View.VISIBLE
                    } else {
                        binding.wishTreeAddBtn.visibility = View.GONE
                    }
                } else { // 등록된 소원이 없으면
                    binding.wishTreeAddBtn.visibility = View.VISIBLE
                }
            } else if (it.status == Status.LOADING) {
                // 로딩이면 일단 아무것도 하지 않음
            } else {
                Toast.makeText(requireContext(), R.string.unknownErrorMessage, Toast.LENGTH_SHORT).show()
            }
        }


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
    }

//    fun showStyleDialog(url: String) {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_style, null)
//
//        val dialogphoto = dialogView.findViewById<ImageView>(R.id.dialog_image)
//        Glide.with(dialogphoto).load(Uri.parse(url))
//            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//            .centerInside()
//            .into(dialogphoto)
//
//        val adb = android.app.AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
//            .setView(dialogView)
//        val dialog = adb.create()
//        val params: WindowManager.LayoutParams = dialog.window!!.attributes;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        dialog.window!!.attributes = params
//
//        //나오는부분말고는 투명하게 해주는것
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//        dialog.show()
//    }

}