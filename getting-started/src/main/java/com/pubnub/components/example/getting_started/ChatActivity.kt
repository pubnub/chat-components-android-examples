package com.pubnub.components.example.getting_started

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
import com.pubnub.components.example.getting_started.ui.theme.AppTheme
import com.pubnub.components.example.getting_started.ui.view.Chat

class ChatActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePubNub()
        setContent {
            AppTheme(pubNub = pubNub, database = ChatApplication.database) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Chat.View(Settings.channelId)
                }
            }
        }
    }

    override fun onDestroy() {
        destroyPubNub()
        super.onDestroy()
    }

    private fun initializePubNub() {
        pubNub = PubNub(
            PNConfiguration(UserId(Settings.userId)).apply {
                publishKey = "demo"
                subscribeKey = "demo"
                logVerbosity = PNLogVerbosity.BODY
                origin = "10.0.2.2:8090"
                secure = false
            }
        )
    }

    private fun destroyPubNub() {
        pubNub.destroy()
    }
}
