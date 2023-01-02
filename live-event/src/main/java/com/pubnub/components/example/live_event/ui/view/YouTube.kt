package com.pubnub.components.example.live_event.ui.view

import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentXKt
import com.pubnub.components.example.live_event.R
import timber.log.Timber

@Composable
fun YouTubeView(apiKey: String, videoId: String) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val view = FragmentContainerView(it).apply {
                id = R.id.fragment_container_view_tag
            }
            val fragment = YouTubePlayerSupportFragmentXKt().apply {
                initialize(
                    apiKey,
                    object : YouTubePlayer.OnInitializedListener {
                        override fun onInitializationFailure(
                            provider: YouTubePlayer.Provider,
                            result: YouTubeInitializationResult
                        ) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_init),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onInitializationSuccess(
                            provider: YouTubePlayer.Provider,
                            player: YouTubePlayer,
                            wasRestored: Boolean
                        ) {
                            player.setOnFullscreenListener { isFullscreen ->
                                val newOrientation = if (isFullscreen)
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                else
                                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                                activity?.requestedOrientation = newOrientation
                            }
                            if (!wasRestored) {
                                player.cueVideo(videoId)
                            }
                        }
                    },
                )
            }
            fragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container_view_tag, fragment)
            }
            view
        },
        modifier = Modifier.fillMaxSize()
    )
}
