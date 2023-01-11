package com.pubnub.components.example.telehealth.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument


sealed class Screen(
    val route: String,
    open val arguments: List<NamedNavArgument> = emptyList(),
) {
    object Login : Screen("login")
    object ChannelList : Screen("channels")
    object MessageList : Screen("channel") {
        const val channelIdArg = "channelId"
        val routeWithArgs =
            "$route/{$channelIdArg}"
        override val arguments = listOf(
            navArgument(channelIdArg) { type = NavType.Companion.StringType },
        )

        fun createRoute(
            channelId: String,
        ) = "$route/$channelId"
    }
}