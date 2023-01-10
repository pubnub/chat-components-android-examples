package com.pubnub.components.example.telehealth.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pubnub.components.example.telehealth.ChatApplication
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.ui.theme.AppTheme
import com.pubnub.components.example.telehealth.ui.view.Channel
import com.pubnub.components.example.telehealth.ui.view.Chat
import com.pubnub.components.example.telehealth.ui.view.Login

@Composable
fun NavGraph(navController: NavHostController) {
    AppTheme(userId = ChatApplication.userId) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        )
        {
            composable(route = Screen.Login.route) {
                Login.View(
                    onLoginSuccess = { userId, userType ->
                        navController.navigate(Screen.Channel.createRoute(userId, userType))
                    }
                )
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

