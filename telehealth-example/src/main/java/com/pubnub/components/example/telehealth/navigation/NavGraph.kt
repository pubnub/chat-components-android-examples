package com.pubnub.components.example.telehealth.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pubnub.components.example.telehealth.ui.view.Channel
import com.pubnub.components.example.telehealth.ui.view.Chat
import com.pubnub.components.example.telehealth.ui.view.Login
import com.pubnub.components.example.telehealth.viewmodel.LoginViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val loginViewModel: LoginViewModel = LoginViewModel.default()
    val user = loginViewModel.getLoggedUser()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            Login.View { _, _ ->
                navController.navigate(Screen.ChannelList.route)
            }
        }
        composable(route = Screen.ChannelList.route) {
            if (user != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Channel.View(userId = user.id, userType = user.type,
                        onChannelSelected = { channelId ->
                            navController.navigate(
                                Screen.MessageList.createRoute(
                                    channelId = channelId
                                )
                            )
                        })
                }
            }
        }
        composable(
            route = Screen.MessageList.routeWithArgs,
            arguments = Screen.MessageList.arguments,
        ) { navBackStackEntry ->
            val channelId =
                navBackStackEntry.arguments?.getString(Screen.MessageList.channelIdArg)
            if (user != null && channelId != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Chat.View(
                        channelId = channelId,
                        navController = navController,
                    )
                }
            }
        }
    }
}
