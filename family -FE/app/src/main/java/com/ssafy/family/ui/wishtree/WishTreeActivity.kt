package com.ssafy.family.ui.wishtree

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityWishTreeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class WishTreeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishTreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishTreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.wishtree_frame_layout, WishTreeFragment()).commit()
    }

}