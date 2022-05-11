package com.ssafy.family.ui.status

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityStatusBinding
import com.ssafy.family.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatusBinding
    private val statusViewModel by viewModels<StatusViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.status_activity_fragment_layout, EditStatusFragment())
            .commit()
        statusViewModel.getFamilyPicture()
//        statusViewModel.familyPicture.observe(this) {
//            Log.d(TAG, "familyPicture : ${it.data?.dataset}")
//        }
        statusViewModel.getMyStatus()
    }
}