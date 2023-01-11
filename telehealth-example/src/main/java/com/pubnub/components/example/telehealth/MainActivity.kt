package com.pubnub.components.example.telehealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.example.telehealth.navigation.NavGraph
import com.pubnub.components.example.telehealth.repository.FakePersitentStorage
import com.pubnub.components.example.telehealth.ui.theme.AppTheme
import com.pubnub.components.example.telehealth_example.BuildConfig

class MainActivity : ComponentActivity() {
    private val storage = FakePersitentStorage
    val pubNub: PubNub by lazy {
        PubNub(
            PNConfiguration(userId = UserId(storage.user.value!!.id)).apply {
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.NONE
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme(pubNub = if (storage.user.value != null) pubNub else null) {
                NavGraph(navController = navController)
            }
        }
    }
}