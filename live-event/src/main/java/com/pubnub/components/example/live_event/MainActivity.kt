package com.pubnub.components.example.live_event

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.example.live_event.ui.navigation.Screen
import com.pubnub.components.example.live_event.ui.theme.AppTheme
import com.pubnub.components.example.live_event.ui.view.Chat

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : AppCompatActivity() {

    private lateinit var pubNub: PubNub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initializePubNub()

        setContent {
            val navController = rememberAnimatedNavController()
            AppTheme(pubNub = pubNub, database = LiveEventApplication.database) {
                AnimatedNavHost(navController, startDestination = Screen.Channel.route) {
                    composable(
                        route = Screen.Channel.route,
                        arguments = Screen.Channel.arguments,
                    ) { navBackStackEntry ->
                        val channelId = navBackStackEntry.arguments?.getString("channelId")
                            ?: Settings.channelId
                        Chat.View(channelId)
                    }
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
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.BODY
            }
        )
    }

    private fun destroyPubNub() {
        pubNub.destroy()
    }
}
