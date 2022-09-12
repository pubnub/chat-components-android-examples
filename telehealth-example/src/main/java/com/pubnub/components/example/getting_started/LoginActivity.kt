package com.pubnub.components.example.getting_started

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.pubnub.api.PubNub
import com.pubnub.components.example.getting_started.ui.view.Login

class LoginActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultDataRepository = DefaultDataRepository(resources)
        setContent {
            Login.View(context = this@LoginActivity, defaultDataRepository = defaultDataRepository)
        }
    }
}