package com.pubnub.components.example.telehealth.navigation

import androidx.navigation.navArgument


sealed class Screens(val route: String) {
    object Login : Screens("login_screen")
    object Channel : Screens("channel_screen") {
        const val userParametersArg = "user_parameters"
        val routeWithArgs = "$route/{$userParametersArg}"
        val arguments = listOf(
            navArgument(userParametersArg) { type = UserParametersType() }
        )
    }
    object MessageList : Screens("message_list_screen") {
        const val chatParametersArg = "chat_parameters"
        val routeWithArgs = "$route/{$chatParametersArg}"
        val arguments = listOf(
            navArgument(chatParametersArg) { type = ChatParametersType() }
        )
    }
}