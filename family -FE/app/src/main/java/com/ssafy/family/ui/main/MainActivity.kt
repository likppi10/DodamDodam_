package com.ssafy.family.ui.main

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.family.R
import com.ssafy.family.config.ApplicationClass.Companion.livePush
import com.ssafy.family.databinding.ActivityMainBinding
import com.ssafy.family.ui.main.bottomFragment.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val SP_NAME = "fcm_message"
    var pressedTime =0

    companion object {
        // Notification Channel ID
        const val channel_id = "FAMILY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moveFragment(HomeFragment())

        binding.mainBottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment ->moveFragment(HomeFragment())
                R.id.chattingFragment-> {
                    clearbadge()
                    moveFragment(ChattingFragment())
                }
                R.id.calendarFragment->moveFragment(CalendarFragment())
                R.id.albumFragment->moveFragment(AlbumFragment())
                R.id.settingFragment->moveFragment(SettingFragment())
                else -> true
            }
            true
        }

        livePush.observe(this) {
            if (livePush.value!! > 0) {
                binding.badge.visibility = View.VISIBLE
                binding.badge.setNumber(readSharedPreference("fcm").size)
            } else {
                binding.badge.visibility = View.GONE
            }
        }
    }

    fun moveFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit()
    }

    fun clearbadge(){
        val fcmList = mutableListOf<String>() as ArrayList<String>
        writeSharedPreference("fcm", fcmList)
        livePush.postValue(readSharedPreference("fcm").size)
    }

    private fun writeSharedPreference(key:String, value:ArrayList<String>){
        val sp = getSharedPreferences(SP_NAME, MODE_PRIVATE)
        val editor = sp.edit()
        val gson = Gson()
        val json: String = gson.toJson(value)
        editor.putString(key, json)
        editor.apply()
    }

    private fun readSharedPreference(key: String): ArrayList<String> {
        val sp = binding.root.context.getSharedPreferences(SP_NAME, FirebaseMessagingService.MODE_PRIVATE)
        val gson = Gson()
        val json = sp.getString(key, "") ?: ""
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val obj: ArrayList<String> = gson.fromJson(json, type) ?: ArrayList()
        return obj
    }

    override fun onBackPressed() {
        if (pressedTime === 0) {
            Toast.makeText(this@MainActivity, " 한 번 더 누르면 종료돼요.", Toast.LENGTH_LONG).show()
            pressedTime = System.currentTimeMillis().toInt()
        } else {
            val seconds = (System.currentTimeMillis().toInt() - pressedTime)
            if (seconds > 2000) {
                Toast.makeText(this@MainActivity, " 한 번 더 누르면 종료돼요.", Toast.LENGTH_LONG).show()
                pressedTime = 0
            } else {
                super.onBackPressed()
            }
        }
    }

}