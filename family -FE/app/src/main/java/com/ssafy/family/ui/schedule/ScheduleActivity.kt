package com.ssafy.family.ui.schedule

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class ScheduleActivity : AppCompatActivity() {

    lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sId = intent.getLongExtra("sID", -1L)
        if(sId == -1L){
            supportFragmentManager.beginTransaction()
                .replace(R.id.schedule_frame, AddScheduleFragment())
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.schedule_frame, DetailScheduleFragment.newInstance(sId))
                .commit()
        }
    }

    fun changeHeader(title: String, left: String, right: String) {
        binding.scheduleTopInclude.scheduleTitle.text = title
        binding.scheduleButtonInclude.button.text = left
        binding.scheduleButtonInclude.button2.text = right
    }

}