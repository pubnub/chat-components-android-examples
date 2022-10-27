package com.pubnub.components.example.telehealth

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.pubnub.components.example.telehealth.ui.view.Login

class LoginActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultDataRepository = DefaultDataRepository(resources)
        setContent {
            Login.View(defaultDataRepository = defaultDataRepository)
        }
    }
}