package com.pubnub.components.example.telehealth

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import com.pubnub.components.example.telehealth.ui.view.Channel

class ChannelActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parameters = intent.extras?.getParcelable<Parameters>(PARAMETERS_BUNDLE_KEY)
        parameters?.userId?.let { initializePubNub(it) }
        setContent {
            AppTheme(pubNub = pubNub) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (parameters != null) {
                        Channel.View(parameters)
                    }
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

    companion object
}