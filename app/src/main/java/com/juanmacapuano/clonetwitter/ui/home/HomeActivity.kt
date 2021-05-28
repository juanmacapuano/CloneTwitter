package com.juanmacapuano.clonetwitter.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}