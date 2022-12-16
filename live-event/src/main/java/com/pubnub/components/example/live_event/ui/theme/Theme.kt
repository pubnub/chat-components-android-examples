package com.pubnub.components.example.live_event.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.pubnub.api.PubNub
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.asPubNub
import com.pubnub.components.chat.provider.ChatProvider
import com.pubnub.components.data.Database

private val DarkColorPalette = darkColors(
    primary = PhilippineSilver,
    primaryVariant = RaisinBlack,
    secondary = Cultured,
    background = Gunmetal,
    surface = RaisinBlack,
)

@Composable
fun AppTheme(
    pubNub: PubNub,
    database: DefaultDatabase = Database.initialize(LocalContext.current),
    content: @Composable() () -> Unit,
) {
    val colors = DarkColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {

        ChatProvider(pubNub, database.asPubNub()) {
            content()
        }
    }
}
