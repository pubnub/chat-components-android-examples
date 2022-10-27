package com.pubnub.components.example.telehealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.example.getting_started.BuildConfig
import com.pubnub.components.example.telehealth.dto.Parameters
import com.pubnub.components.example.telehealth.dto.Parameters.Companion.PARAMETERS_BUNDLE_KEY
import com.pubnub.components.example.telehealth.ui.theme.AppTheme
import com.pubnub.components.example.telehealth.ui.view.Chat

class ChatActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parameters = intent.extras?.getParcelable<Parameters>(PARAMETERS_BUNDLE_KEY)
        if (parameters != null) {
            initializePubNub(parameters.userId)
        }
        setContent {
            AppTheme(pubNub = pubNub) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (parameters != null) {
                        Chat.View(parameters)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        destroyPubNub()
        super.onDestroy()
    }

    private fun initializePubNub(userId: String) {
        pubNub = PubNub(
            PNConfiguration(userId = UserId(userId)).apply {
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.NONE
            }
        )
    }

    private fun destroyPubNub() {
        pubNub.destroy()
    }
}
