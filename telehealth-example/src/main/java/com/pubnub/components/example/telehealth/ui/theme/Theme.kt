package com.pubnub.components.example.telehealth.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pubnub.api.PubNub
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.asPubNub
import com.pubnub.components.chat.provider.ChatProvider
import com.pubnub.components.chat.provider.RepositoryProvider
import com.pubnub.components.chat.ui.component.common.ShapeThemeDefaults
import com.pubnub.components.chat.ui.component.common.TextThemeDefaults
import com.pubnub.components.chat.ui.component.common.ThemeDefaults
import com.pubnub.components.chat.ui.component.message.MessageListTheme
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
    pubNub: PubNub? = null,
    database: DefaultDatabase = ChatApplication.database,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    val colors = if (darkTheme) DarkColorPalette
    else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        if (pubNub == null) {
            RepositoryProvider(ChatApplication.database.asPubNub()) {
                content()
            }
        } else {
            ChatProvider(
                pubNub = pubNub,
                database = database.asPubNub(),
            ) {
                content()
            }
        }

    }
}

@Composable
fun ChatMessageTheme(): MessageListTheme {
    return ThemeDefaults.messageList(
        messageOwn = ThemeDefaults.message(
            text = TextThemeDefaults.text(
                Modifier
                    .background(color = MessageOwnBackgroundColor)
                    .padding(2.dp)

            ),
            shape = ShapeThemeDefaults.shape(shape = Shapes.medium),
            modifier = Modifier
                .clip(Shapes.large)
                .fillMaxSize()
        ),
        message = ThemeDefaults.message(
            text = TextThemeDefaults.text(
                modifier = Modifier
                    .background(color = MessageBackgroundColor)
                    .padding(2.dp)
            ),
            shape = ShapeThemeDefaults.shape(shape = Shapes.medium),
            modifier = Modifier
                .clip(Shapes.large)
                .fillMaxSize()
        )
    )
}