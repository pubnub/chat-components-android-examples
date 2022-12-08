package com.pubnub.components.example.telehealth.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.example.telehealth.ChatApplication
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.dto.UserParameters
import com.pubnub.components.example.telehealth.extensions.parcelable
import com.pubnub.components.example.telehealth.ui.theme.AppTheme
import com.pubnub.components.example.telehealth.ui.view.Channel
import com.pubnub.components.example.telehealth.ui.view.Chat
import com.pubnub.components.example.telehealth.ui.view.Login
import com.pubnub.components.example.telehealth_example.BuildConfig

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route)
    {
        composable(route = Screens.Login.route) {
            Login.View(
                onLoginSuccess = { userParameters ->
                    val json = Uri.encode(Gson().toJson(userParameters))
                    navController.navigate("${Screens.Channel.route}/$json")
                }
            )
        }
        composable(
            route = Screens.Channel.routeWithArgs,
            arguments = Screens.Channel.arguments,
        ) { navBackStackEntry ->
            val userParameters =
                navBackStackEntry.arguments?.parcelable<UserParameters>(Screens.Channel.userParametersArg)
            if (userParameters != null) {
                initializePubNub(userParameters.userId)
                AppTheme(pubNub = ChatApplication.pubNub) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Channel.View(userParameters,
                            onChannelSelected = { chatParameters ->
                                val json = Uri.encode(Gson().toJson(chatParameters))
                                navController.navigate("${Screens.MessageList.route}/$json")
                            })
                    }
                }
            }
        }
        composable(
            route = Screens.MessageList.routeWithArgs,
            arguments = Screens.MessageList.arguments,
        ) { navBackStackEntry ->
            val chatParameters =
                navBackStackEntry.arguments?.parcelable<ChatParameters>(Screens.MessageList.chatParametersArg)
            if (chatParameters != null) {
                AppTheme(pubNub = ChatApplication.pubNub) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Chat.View(chatParameters, navController)
                    }
                }
            }
        }
    }
}

private fun initializePubNub(userId: String) {
    ChatApplication.pubNub = PubNub(
        PNConfiguration(userId = UserId(userId)).apply {
            publishKey = BuildConfig.PUBLISH_KEY
            subscribeKey = BuildConfig.SUBSCRIBE_KEY
            logVerbosity = PNLogVerbosity.NONE
        }
    )
}