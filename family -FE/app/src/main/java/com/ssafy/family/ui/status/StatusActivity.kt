package com.ssafy.family.ui.status

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityStatusBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class StatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatusBinding
    private val statusViewModel by viewModels<StatusViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("to") == "change"){
            supportFragmentManager.beginTransaction().replace(R.id.status_activity_fragment_layout, SelectFamilyPictureFragment()).commit()
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.status_activity_fragment_layout, EditStatusFragment()).commit()
        }
    }

}