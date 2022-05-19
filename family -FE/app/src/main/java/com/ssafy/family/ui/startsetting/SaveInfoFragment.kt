package com.ssafy.family.ui.startsetting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.data.remote.res.MyProfile
import com.ssafy.family.databinding.FragmentSaveInfoBinding
import com.ssafy.family.ui.changeProfileImage.ChangeProfileImageActivity
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.status.StatusActivity
import com.ssafy.family.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SaveInfoFragment : Fragment() {

    private lateinit var binding: FragmentSaveInfoBinding
    private val familyViewModel by activityViewModels<StartSettingViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private lateinit var getProfileImage: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
    var role: String = "아빠"
    var first = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ChangeProfileImageActivity 런쳐 등록
        getProfileImage = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val changedImage: String? = it.data?.getStringExtra("imageUri")
                if (changedImage == "null") {
                    imageUri = null
                    Glide.with(binding.saveInfoProfileImage).load(R.drawable.image_fail).into(binding.saveInfoProfileImage)
                } else {
                    val changedImageUri = Uri.parse(changedImage)
                    imageUri = changedImageUri
                    Glide.with(binding.saveInfoProfileImage).load(changedImageUri).into(binding.saveInfoProfileImage)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 스피너(다이얼로그 형) 한개짜리 설정
        val Data = resources.getStringArray(R.array.family_role)
        val Adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, Data)
        binding.saveInfoSpinner.adapter = Adapter
        binding.saveInfoSpinner.setSelection(0)
        binding.saveInfoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // data setting
                    binding.saveInfoSpinnerNumber.setSelection(0)
                    binding.saveInfoSpinnerRole.setSelection(position)
                    role = binding.saveInfoSpinner.selectedItem.toString()
                    // vision toggle
                    spinnerToggle(position)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        // 스피너(다이얼로그 형) 두개짜리 설정
        val numberData = resources.getStringArray(R.array.family_number)
        val numberAdapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, numberData)
        val roleData = resources.getStringArray(R.array.family_role)
        val roleAdapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, roleData)
        binding.saveInfoSpinnerNumber.adapter = numberAdapter
        binding.saveInfoSpinnerRole.adapter = roleAdapter
        // 첫째 ~ 넷째 선택
        binding.saveInfoSpinnerNumber.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long) {
                    role = binding.saveInfoSpinnerNumber.selectedItem.toString() + " " + binding.saveInfoSpinnerRole.selectedItem.toString()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        // 위에서 첫쨰~넷째 선택 후 role 선택
        binding.saveInfoSpinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // data setting
                    binding.saveInfoSpinner.setSelection(position)
                    role = binding.saveInfoSpinnerNumber.selectedItem.toString() + " " + binding.saveInfoSpinnerRole.selectedItem.toString()
                    // vision toggle
                    spinnerToggle(position)
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        // 이미지 선택 페이지 이동
        binding.saveInfoProfileImage.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileImageActivity::class.java)
            intent.putExtra("imageUri", imageUri.toString())
            getProfileImage.launch(intent)
        }

        // Data 관련 코드
        initView()
    }

    private fun initView() {

        // "다음" 버튼 클릭 리스너 등록
        // 가족 생성
        binding.saveInfoMoveNextBtn.setOnClickListener {
            if (isValidForm()) {
                if(requireActivity().intent.getStringExtra("to") == "edit"){
                    updateProfile(role)
                }else{
                    val viewModelFamilyId = familyViewModel.checkFamilyCodeInfoRes.value?.data?.dataset?.familyId
                    // 가족코드 검증을 하고 온 경우 : 뷰모델의 familyId가 존재 -> 기존 가족에 가입
                    if (viewModelFamilyId is Int) {
                        joinFamily(role, viewModelFamilyId)
                    } else { // 바로 온 경우 가족 및 프로필 생성)
                        createFamily(role)
                    }
                }
            }
        }

        if(requireActivity().intent.getStringExtra("to") == "edit"){
            familyViewModel.getMyProfile()
            binding.saveInfoMoveNextBtn.text = "수정하기"
            // 상단 텍스트 수정
            (activity as StartSettingActivity).changeTopMessage("'나'를 수정하세요!")
        }else{
            // 상단 텍스트 수정
            (activity as StartSettingActivity).changeTopMessage("'나'를 저장하세요!")
        }

        // LiveData observe
        familyViewModel.getMyProfileRes.observe(requireActivity()) {
            if (it.status == Status.SUCCESS){
                setMyProfile(it.data!!.data!!)
            } else if(it.status == Status.ERROR) {
                Toast.makeText(requireContext(), "프로필 생성에 실패했어요.", Toast.LENGTH_SHORT).show()
            }
        }

        // LiveData observe
        familyViewModel.familyInfoResponseLiveData.observe(requireActivity()) {
            if (it.status == Status.SUCCESS){
                // 가족 생성 요청 성공 시 sharedpreference에 familyId 저장
                LoginUtil.setFamilyId(it.data!!.dataset!!.familyId.toString())
                LoginUtil.setProfileId(it.data.dataset!!.profileId.toString())
                getFCM()
            } else if(it.status == Status.ERROR) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        // LiveData observe
        familyViewModel.updateMyProfileRes.observe(requireActivity()) {
            if (it.status == Status.SUCCESS){
                Toast.makeText(requireContext(), "오늘의 상태 수정 완료!", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            } else if(it.status == Status.ERROR) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.baseResponse.observe(requireActivity()) {
            // fcm 받아와 졌는지 확인
            when (it.status) {
                Status.SUCCESS -> { // 로그인 성공
                    // 토스트메시지 띄우고 화면 이동
                    Toast.makeText(requireContext(), "프로필 생성에 성공했어요.", Toast.LENGTH_SHORT).show()
                    var intent = Intent(requireContext(), StatusActivity::class.java)
                    if(first){
                        intent.putExtra("to", "first")
                    }
                    startActivity(intent)
                    requireActivity().finish()
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        }

        // 닉네임 유효성 검사 통과 시 에러메시지 삭제
        binding.saveInfoInputNickname.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidNickName(input)) {
                dismissErrorOnNickName()
            }
        }

        // 생년월일 유효성 검사 통과 시 에러메시지 삭제
        binding.saveInfoInputBirthday.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidBirthDay(input)) {
                dismissErrorOnBirthday()
            }
        }
    }

    // 초기 데이터 세팅
    private fun setMyProfile(data: MyProfile) {
        binding.saveInfoInputNickname.setText(data.nickname)
        var birth = data.birthday.split("-")
        binding.saveInfoInputBirthday.setText(birth[0]+birth[1]+birth[2])
        role = data.role

        if(data.imagePath == null){
            Glide.with(binding.saveInfoProfileImage).load(R.drawable.image_fail).into(binding.saveInfoProfileImage)
        }else{
            Glide.with(binding.saveInfoProfileImage).load(data.imagePath).into(binding.saveInfoProfileImage)
            imageUri = Uri.parse(data.imagePath)
        }
        if(role == "아빠"){
            binding.saveInfoSpinner.setSelection(0)
        }else if(role == "엄마"){
            binding.saveInfoSpinner.setSelection(1)
        }else{
            var roleDetail = role.split(" ")

            if(roleDetail[0] == "딸"){
                spinnerToggle(3)
                binding.saveInfoSpinnerNumber.setSelection(0)
                binding.saveInfoSpinnerRole.setSelection(3)
            }else if(roleDetail[0] == "아들"){
                spinnerToggle(2)
                binding.saveInfoSpinnerNumber.setSelection(0)
                binding.saveInfoSpinnerRole.setSelection(2)
            }else{
                if(roleDetail[1] == "딸"){
                    spinnerToggle(3)
                    binding.saveInfoSpinnerRole.setSelection(3)
                }else if(roleDetail[1] == "아들"){
                    spinnerToggle(2)
                    binding.saveInfoSpinnerRole.setSelection(2)
                }
                when {
                    roleDetail[0] == "넷째" -> {
                        binding.saveInfoSpinnerNumber.setSelection(4)
                    }
                    roleDetail[0] == "셋째" -> {
                        binding.saveInfoSpinnerNumber.setSelection(3)
                    }
                    roleDetail[0] == "둘째" -> {
                        binding.saveInfoSpinnerNumber.setSelection(2)
                    }
                    roleDetail[0] == "첫째" -> {
                        binding.saveInfoSpinnerNumber.setSelection(1)
                    }
                    else -> {
                        binding.saveInfoSpinnerNumber.setSelection(0)
                    }
                }
            }
        }
    }

    // 데이터 유효성 검사
    private fun isValidForm(): Boolean {
        val nickname = binding.saveInfoInputNickname.text.toString()
        val birthday = binding.saveInfoInputBirthday.text.toString()
        var flag = 1
        // 닉네임 유효성 검사
        if (InputValidUtil.isValidNickName(nickname)) {
            dismissErrorOnNickName()
        } else {
            flag = 0
            setErrorOnNickName()
        }
        // 생년월일 유효성 검사
        if (InputValidUtil.isValidBirthDay(birthday)) {
            dismissErrorOnBirthday()
        } else {
            flag = 0
            setErrorOnBirthday()
        }
        //유효성 검사 모두 통과 시 flag == 1 (-> true)
        return flag == 1
    }

    // 에러메시지 표시와 관련된 함수들
    private fun setErrorOnNickName() {
        binding.textInputLayoutSaveInfoNickname.error = resources.getString(R.string.nickNameErrorMessage)
    }

    private fun dismissErrorOnNickName() {
        binding.textInputLayoutSaveInfoNickname.error = null
    }

    private fun setErrorOnBirthday() {
        binding.textInputLayoutSaveInfoBirthday.error = resources.getString(R.string.birthdayErrorMessage)
    }

    private fun dismissErrorOnBirthday() {
        binding.textInputLayoutSaveInfoBirthday.error = null
    }

    // ***중요*** 네트워크(profile 데이터 보내고 familyId 받아오는 함수)
    private fun createFamily(role: String) {
        val selectedRole = role.trim()
        val nickname = binding.saveInfoInputNickname.text.toString()
        val birthday = InputValidUtil.makeDay(binding.saveInfoInputBirthday.text.toString())
        var imageFile: File? = null
        var characterPath: String? = null
        if (imageUri != null) { // Uri가 있고
            // 일러스트면
            if (imageUri.toString().contains("https://s3-dodamdodam.s3.ap-northeast-2.amazonaws.com/profileSamples")) {
                characterPath = imageUri.toString()
            } else { // 폰에 저장된 사진이면
                imageFile = getRealFile(imageUri!!)
            }
        }
        first = true
        familyViewModel.createFamily(FamilyReq(selectedRole, nickname, birthday, characterPath), imageFile)
    }

    private fun joinFamily(role: String, familyId: Int) {
        val selectedRole = role.trim()
        val nickname = binding.saveInfoInputNickname.text.toString()
        val birthday = InputValidUtil.makeDay(binding.saveInfoInputBirthday.text.toString())
        var imageFile: File? = null
        var characterPath: String? = null
        if (imageUri != null) { // Uri가 있고
            // 일러스트면
            if (imageUri.toString().contains("https://s3-dodamdodam.s3.ap-northeast-2.amazonaws.com/profileSamples")) {
                characterPath = imageUri.toString()
            } else { // 폰에 저장된 사진이면
                imageFile = getRealFile(imageUri!!)
            }
        }
        familyViewModel.joinFamily(FamilyReq(selectedRole, nickname, birthday, characterPath), familyId, imageFile)
    }

    private fun updateProfile(role: String) {
        val selectedRole = role.trim()
        val nickname = binding.saveInfoInputNickname.text.toString()
        val birthday = InputValidUtil.makeDay(binding.saveInfoInputBirthday.text.toString())
        var imageFile: File? = null
        var characterPath: String? = null
        if (imageUri != null) { // Uri가 있고
            // 일러스트면
            if (imageUri.toString().contains("https://s3-dodamdodam.s3.ap-northeast-2.amazonaws.com/profileSamples")) {
                characterPath = imageUri.toString()
            } else { // 폰에 저장된 사진이면
                imageFile = getRealFile(imageUri!!)
            }
        }
        familyViewModel.updateMyProfile(FamilyReq(selectedRole, nickname, birthday, characterPath), imageFile)
    }

    private fun addFCM(fcmToken: AddFcmReq) {
        loginViewModel.addFCM(fcmToken)
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

    // 스피너 UI 토글 함수 (한개 or 두개)
    fun spinnerToggle(role: Int) {
        // 아빠: 0, 엄마: 1, 아들: 2, 딸: 3
        if (role == 0 || role == 1) {
            binding.saveInfoSpinner.visibility = View.VISIBLE
            binding.saveInfoDoubleSpinnerLayout.visibility = View.GONE
        } else {
            binding.saveInfoSpinner.visibility = View.GONE
            binding.saveInfoDoubleSpinnerLayout.visibility = View.VISIBLE
        }
    }

    private fun getRealFile(uri: Uri): File? {
        var uri: Uri? = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        var cursor: Cursor? = uri?.let {
            context?.contentResolver?.query(
                it, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc"
            )
        }
        if (cursor == null || cursor.columnCount < 1) {
            return null
        }
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        if (cursor != null) {
            cursor.close()
            cursor = null
        }
        return File(path)
    }

}
