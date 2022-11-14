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
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.ui.theme.AppTheme
import com.pubnub.components.example.telehealth.ui.view.Channel
import com.pubnub.components.example.telehealth_example.BuildConfig

class ChannelActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatParameters = ChatParameters.fromIntent(intent)
        checkNotNull(chatParameters)
        initializePubNub(chatParameters.userId)
        setContent {
            AppTheme(pubNub = pubNub) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Channel.View(chatParameters)
                }
            }
        }
    }

    override fun onDestroy() {
        destroyPubNub()
        super.onDestroy()
    }

    private fun initializePubNub(uuid: String) {
        pubNub = PubNub(
            PNConfiguration(userId = UserId(uuid)).apply {
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