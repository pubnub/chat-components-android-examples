package com.pubnub.components.example.live_event.ui.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pubnub.components.example.live_event.BuildConfig
import com.pubnub.components.example.live_event.ui.util.Keyboard

@Composable
fun VideoContent() {

    val isVisible by Keyboard.asState()
    AnimatedVisibility(
        visible = !isVisible,
        enter = slideInVertically() + expandVertically(),
        exit = shrinkVertically() + slideOutVertically(),
    ) {
        Box(Modifier.aspectRatio(16 / 9f)) {
            YouTubeView(apiKey = BuildConfig.YOUTUBE_KEY, videoId = "eVS6EfrSA3I")
        }
    }
}
