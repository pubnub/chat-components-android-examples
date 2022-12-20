package com.pubnub.components.example.telehealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.pubnub.components.asPubNub
import com.pubnub.components.chat.provider.RepositoryProvider
import com.pubnub.components.example.telehealth.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepositoryProvider(ChatApplication.database.asPubNub()) {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}