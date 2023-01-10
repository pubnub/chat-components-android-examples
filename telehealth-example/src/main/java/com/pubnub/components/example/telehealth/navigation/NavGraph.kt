package com.pubnub.components.example.telehealth.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.example.telehealth.ChatApplication
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.ui.theme.AppTheme
import com.pubnub.components.example.telehealth.ui.view.Channel
import com.pubnub.components.example.telehealth.ui.view.Chat
import com.pubnub.components.example.telehealth.ui.view.Login
import com.pubnub.components.example.telehealth_example.BuildConfig

@Composable
fun NavGraph(navController: NavHostController) {
    AppTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        )
        {
            composable(route = Screen.Login.route) {
                Login.View { userId, userType ->
                    //TODO initialization
                    initializePubNub(userId)
                    navController.navigate(Screen.Channel.createRoute(userId, userType))
                }
            }
            composable(
                route = Screen.Channel.routeWithArgs,
                arguments = Screen.Channel.arguments,
            ) { navBackStackEntry ->
                val userId = navBackStackEntry.arguments?.getString(Screen.Channel.userIdArg)
                val userType = navBackStackEntry.arguments?.getString(Screen.Channel.userTypeArg)
                if (userId != null && userType != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Channel.View(userId = userId, userType = userType,
                            onChannelSelected = { chatParameters ->
                                navController.navigate(Screen.MessageList.createRoute(
                                    userId = chatParameters.userId,
                                    userType = chatParameters.userType,
                                    channelId = chatParameters.channelId,
                                    secondUserId = chatParameters.secondUserId,
                                    secondUserName = chatParameters.secondUserName))
                            })
                    }
                }
            }
            composable(
                route = Screen.MessageList.routeWithArgs,
                arguments = Screen.MessageList.arguments,
            ) { navBackStackEntry ->
                val userId = navBackStackEntry.arguments?.getString(Screen.MessageList.userIdArg)
                val userType =
                    navBackStackEntry.arguments?.getString(Screen.MessageList.userTypeArg)
                val channelId =
                    navBackStackEntry.arguments?.getString(Screen.MessageList.channelIdArg)
                val secondUserId =
                    navBackStackEntry.arguments?.getString(Screen.MessageList.secondUserIdArg)
                val secondUserName =
                    navBackStackEntry.arguments?.getString(Screen.MessageList.secondUserNameArg)
                if (userId != null && userType != null && channelId != null && secondUserId != null && secondUserName != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Chat.View(ChatParameters(userId = userId,
                            userType = userType,
                            channelId = channelId,
                            secondUserId = secondUserId,
                            secondUserName = secondUserName), navController)
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

