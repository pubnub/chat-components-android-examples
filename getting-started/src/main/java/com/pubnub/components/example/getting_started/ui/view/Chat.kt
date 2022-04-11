package com.pubnub.components.example.getting_started.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.pubnub.components.chat.ui.component.input.MessageInput
import com.pubnub.components.chat.ui.component.input.renderer.AnimatedTypingIndicatorRenderer
import com.pubnub.components.chat.ui.component.message.MessageList
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.presence.Presence
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.framework.data.ChannelId
import com.pubnub.framework.data.UserId
import kotlinx.coroutines.flow.Flow

object Chat {

    @Composable
    internal fun Content(
        messages: Flow<PagingData<MessageUi>>,
        presence: Presence? = null,
        onMemberSelected: (UserId) -> Unit = {},
    ) {
        val localFocusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                }
        ) {
            MessageList(
                messages = messages,
                presence = presence,
                onMemberSelected = onMemberSelected,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, true),
            )

            MessageInput(
                typingIndicator = true,
                typingIndicatorRenderer = AnimatedTypingIndicatorRenderer,
            )
        }
    }

    @Composable
    fun View(channelId: ChannelId) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.defaultWithMediator(channelId)
        val messages = remember { messageViewModel.getAll() }
        // endregion

        CompositionLocalProvider(
            LocalChannel provides channelId
        ) {

            Scaffold(
                content = {
                    Content(
                        messages = messages,
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun ChatPreview() {
    Chat.View("channel.lobby")
}
