package com.juanmacapuano.clonetwitter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.juanmacapuano.clonetwitter.service.UserPreferences
import com.juanmacapuano.clonetwitter.tools.startNewActivity
import com.juanmacapuano.clonetwitter.ui.auth.AuthActivity
import com.juanmacapuano.clonetwitter.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, Observer {
            val activity = if (it == null) AuthActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })

        // startNewActivity(AuthActivity::class.java)
    }
}