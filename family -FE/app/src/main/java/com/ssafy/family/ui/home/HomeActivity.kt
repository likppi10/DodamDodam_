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
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.family.R
import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.databinding.ActivityHomeBinding
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.ui.schedule.AddScheduleFragment
import com.ssafy.family.ui.startsetting.StartSettingActivity
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.PermissionUtil
import com.ssafy.family.util.SharedPreferencesUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        permissionUtil.permissionListener = object : PermissionUtil.PermissionListener {
            override fun run() {
                init()
            }
        }
    }
    fun init(){
        if (ApplicationClass.sSharedPreferences.getString(ApplicationClass.JWT) != null) {
            // TODO: 첫 접속일시 분기 만들어야함
            // TODO: 토큰 만료됐을시 분기 만들어야함
            loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
            getFCM()

        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_frame, LoginFragment())
                .commit()
        }
        loginViewModel.makeRefreshLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    startActivity(Intent(this, MainActivity::class.java))
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