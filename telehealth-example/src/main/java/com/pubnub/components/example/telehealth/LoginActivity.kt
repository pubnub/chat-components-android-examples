package com.pubnub.components.example.telehealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pubnub.components.asPubNub
import com.pubnub.components.example.telehealth.ui.view.Login

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepositoryProvider(ChatApplication.database.asPubNub()) {
                Login.View()
            }
        }
    }
}