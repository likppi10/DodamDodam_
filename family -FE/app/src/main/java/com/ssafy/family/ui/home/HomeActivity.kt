package com.ssafy.family.ui.home

import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.family.R
import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.databinding.ActivityHomeBinding
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.ui.schedule.AddScheduleFragment
import com.ssafy.family.ui.startsetting.StartSettingActivity
import com.ssafy.family.util.*
import com.ssafy.family.ui.status.StatusActivity
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.PermissionUtil
import com.ssafy.family.util.SharedPreferencesUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    lateinit var dialog: Dialog
    lateinit var permissionUtil: PermissionUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionUtil = PermissionUtil(this)
        Log.d("dddd", "onMessageReceived: "+readSharedPreference("fcm").size)
        // 오늘 첫 로그인 확인
        loginViewModel.getFirstLoginToday()
        permissionUtil.permissionListener = object : PermissionUtil.PermissionListener {
            override fun run() {
                init()
            }
        }
    }
    val SP_NAME = "fcm_message"
     private fun readSharedPreference(key:String): ArrayList<String>{
        val sp = getSharedPreferences(SP_NAME, MODE_PRIVATE)
        val gson = Gson()
        val json = sp.getString(key, "") ?: ""
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val obj: ArrayList<String> = gson.fromJson(json, type) ?: ArrayList()
        return obj
    }

    fun init(){
        if (ApplicationClass.sSharedPreferences.getString(ApplicationClass.JWT) != null) {
            // TODO: 토큰 만료됐을시 분기 만들어야함
            loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)


        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_frame, LoginFragment())
                .commit()
        }
        loginViewModel.makeRefreshLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    getFCM()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    getFCM()
                }
            }
        }
        loginViewModel.baseResponse.observe(this) {
            // fcm 받아와 졌는지 확인
            when (it.status) {
                Status.SUCCESS -> { // 로그인 성공
                    dismissLoading()
                    // 분기처리
                    // 1) 가입 후 첫 로그인인가? = LoginUtil 내에 familyId가 있는가?
                    val familyId = LoginUtil.getFamilyId()
                    Log.d(TAG, "HomeActivity - init() familyId : $familyId")
                    if (familyId == "0") { // 가입한 Family 없음
                        startActivity(Intent(this, StartSettingActivity::class.java))
                    } else { // 가입한 Family 있음
                        // 2) 오늘 첫 로그인인가? = 오늘의 미션이 있는가?(missionContent)
                        val missionContent = loginViewModel.checkFirstLoginToday.value?.data?.dataSet?.missionContent
                        if (missionContent == null) { // 오늘 첫 로그인
                            startActivity(Intent(this, StatusActivity::class.java))
                        } else { // 첫 로그인 아님
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, ErrUtil.setErrorMsg(it.message), Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        checkPermissions()
    }
    private fun checkPermissions() {
        if(!permissionUtil.checkPermissions(listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))) {
            permissionUtil.requestPermissions()
        } else {
            init()
        }
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
            Log.d("dddd", "getFCM: "+task.result!!)
            addFCM(AddFcmReq(task.result!!))
        })
        createNotificationChannel(MainActivity.channel_id, "ssafy")
    }

    // NotificationChannel 설정
    private fun createNotificationChannel(id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)

            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
//        binding.homePageLogo.visibility = View.VISIBLE
        binding.homePageAppName.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
//        binding.homePageLogo.visibility = View.GONE
        binding.homePageAppName.visibility = View.GONE
    }
}