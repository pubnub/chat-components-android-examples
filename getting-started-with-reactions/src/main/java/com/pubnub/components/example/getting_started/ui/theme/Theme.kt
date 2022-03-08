package com.pubnub.components.example.getting_started.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.pubnub.api.PubNub
import com.pubnub.components.chat.provider.ChatProvider

private val DarkColorPalette = darkColors(
    primary = Light4,
    primaryVariant = Amaranth,
    secondary = Light4,
    onPrimary = Amaranth,
)

private val LightColorPalette = lightColors(
    primary = Light4,
    primaryVariant = Amaranth,
    onPrimary = Amaranth,
    secondary = Light4,
    onSecondary = Amaranth,
    onSurface = DustyGray,
    onBackground = MineShaft,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

//private val DarkColorPalette = darkColors(
//    primary = Purple200,
//    primaryVariant = Purple700,
//    secondary = Teal200
//)
//
//private val LightColorPalette = lightColors(
//    primary = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200
//
//    /* Other default colors to override
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//    */
//)

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
