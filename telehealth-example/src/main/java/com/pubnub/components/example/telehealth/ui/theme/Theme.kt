package com.pubnub.components.example.telehealth.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pubnub.api.PubNub
import com.pubnub.components.asPubNub
import com.pubnub.components.chat.provider.ChatProvider
import com.pubnub.components.example.telehealth.ChatApplication

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Color.Red,
    primaryVariant = Color.Red,
    secondary = Color.Red
)

@Composable
fun AppTheme(
    pubNub: PubNub,
    darkTheme: Boolean = false,
    content: @Composable() () -> Unit,
) {
    val colors = if (darkTheme) DarkColorPalette
    else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        ChatProvider(
            pubNub = pubNub,
            database = ChatApplication.database.asPubNub(),
        ) {
            content()
        }
    }
}

