package com.pubnub.components.example.telehealth.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument


sealed class Screen(
    val route: String,
    open val arguments: List<NamedNavArgument> = emptyList(),
) {
    object Login : Screen("login_screen")
    object Channel : Screen("channel_screen"
    ) {
        const val userIdArg = "userId"
        const val userTypeArg = "userType"
        val routeWithArgs = "$route/{$userIdArg}/{$userTypeArg}"
        override val arguments = listOf(
            navArgument(userIdArg) { type = NavType.Companion.StringType },
            navArgument(userTypeArg) { type = NavType.Companion.StringType }
        )

        fun createRoute(userId: String, userType: String) = "$route/$userId/$userType"
    }

    object MessageList : Screen(
        "message_list_screen",
    ) {
        const val userIdArg = "userId"
        const val userTypeArg = "userType"
        const val channelIdArg = "channelId"
        const val secondUserIdArg = "secondUserId"
        const val secondUserNameArg = "secondUserName"
        val routeWithArgs =
            "$route/{$userIdArg}/{$userTypeArg}/{$channelIdArg}/{$secondUserIdArg}/{$secondUserNameArg}"
        override val arguments = listOf(
            navArgument(userIdArg) { type = NavType.Companion.StringType },
            navArgument(userTypeArg) { type = NavType.Companion.StringType },
            navArgument(channelIdArg) { type = NavType.Companion.StringType },
            navArgument(secondUserIdArg) { type = NavType.Companion.StringType },
            navArgument(secondUserNameArg) { type = NavType.Companion.StringType },
        )

        fun createRoute(
            userId: String,
            userType: String,
            channelId: String,
            secondUserId: String,
            secondUserName: String,
        ) = "$route/$userId/$userType/$channelId/$secondUserId/$secondUserName"
    }
}