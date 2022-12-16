package com.pubnub.components.example.live_event.ui.util

import android.view.View
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object Keyboard {

    @Composable
    fun asState(view: View = LocalView.current): State<Boolean> {
        val keyboardState = remember { mutableStateOf(false) }
        LaunchedEffect(view) {
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                keyboardState.value = insets.isVisible(WindowInsetsCompat.Type.ime())
                insets
            }
        }
        return keyboardState
    }

    @Composable
    fun isVisible(): Boolean = WindowInsets.ime.getBottom(LocalDensity.current) > 0
}
