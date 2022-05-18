package com.pubnub.components.example.getting_started.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.pubnub.api.PubNub
import com.pubnub.components.chat.provider.ChatProvider

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pubNub: PubNub,
    content: @Composable() () -> Unit,
) {
    val colors = if (darkTheme) DarkColorPalette
    else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {

        ChatProvider(pubNub) {
            content()
        }
    }
}
