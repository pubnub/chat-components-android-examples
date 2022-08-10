package com.pubnub.components.example.getting_started

//import com.pubnub.api.UserId
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
import com.pubnub.framework.data.ChannelId

class ChatActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    private var channelId: ChannelId = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val id = bundle?.getString("channelId")
        val uuid = bundle?.getString("uuid")
        val patientUuid = bundle?.getString("patientUuid")
        val patientName = bundle?.getString("patientName")
        channelId = id as ChannelId
        initializePubNub(uuid ?: "")
        setContent {
            AppTheme(pubNub = pubNub) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Chat.View(channelId, patientUuid ?: "", patientName ?: "")
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
