package com.juanmacapuano.clonetwitter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.juanmacapuano.clonetwitter.databinding.ActivityMainBinding
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}