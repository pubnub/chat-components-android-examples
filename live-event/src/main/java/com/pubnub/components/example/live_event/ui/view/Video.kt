package com.pubnub.components.example.live_event.ui.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.pubnub.components.example.live_event.BuildConfig
import com.pubnub.components.example.live_event.ui.util.Keyboard
import java.util.*

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
