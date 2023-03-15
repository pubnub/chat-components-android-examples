package com.pubnub.components.example.live_event.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    object Channel : Screen(
        route = "channel/{channelId}",
        arguments = listOf(navArgument("channelId") { type = NavType.StringType }),
    ) {
        fun createRoute(channelId: String) = "channel/$channelId"
    }
}