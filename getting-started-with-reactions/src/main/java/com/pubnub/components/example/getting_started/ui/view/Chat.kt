package com.pubnub.components.example.getting_started.ui.view


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.pubnub.components.chat.ui.component.input.MessageInput
import com.pubnub.components.chat.ui.component.menu.Copy
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.MessageList
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.presence.Presence
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.chat.viewmodel.message.ReactionViewModel
import com.pubnub.framework.data.ChannelId
import kotlinx.coroutines.flow.Flow

object Chat {

    @Composable
    internal fun Content(
        messages: Flow<PagingData<MessageUi>>,
        presence: Presence? = null,
        onMessageSelected: (MessageUi.Data) -> Unit,
        onReactionSelected: ((React) -> Unit)? = null,
    ) {
        val localFocusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                }
        ) {
            MessageList(
                messages = messages,
                presence = presence,
                onMessageSelected = onMessageSelected,
                onReactionSelected = onReactionSelected,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, true),
            )

            MessageInput(
                typingIndicatorEnabled = true,
            )
        }
    }

    @Composable
    fun View(
        channelId: ChannelId,
    ) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.defaultWithMediator()
        val messages = remember(channelId) { messageViewModel.getAll(channelId) }

        val reactionViewModel: ReactionViewModel = ReactionViewModel.default()
        DisposableEffect(channelId) {
            reactionViewModel.bind(channelId)
            onDispose {
                reactionViewModel.unbind()
            }
        }
        // endregion

        var menuVisible by remember { mutableStateOf(false) }
        var selectedMessage by remember { mutableStateOf<MessageUi.Data?>(null) }

        val onDismiss: () -> Unit = { menuVisible = false }
        CompositionLocalProvider(LocalChannel provides channelId) {
            Menu(
                visible = menuVisible,
                message = selectedMessage,
                onDismiss = onDismiss,
                onAction = { action ->
                    when (action) {
                        is Copy -> {
                            messageViewModel.copy(AnnotatedString(action.message.text))
                        }
                        is React -> reactionViewModel.reactionSelected(action)
                        else -> {}
                    }
                    onDismiss()
                }
            )


            Content(
                messages = messages,
                onMessageSelected = {
                    selectedMessage = it
                    menuVisible = true
                },
                onReactionSelected = reactionViewModel::reactionSelected,
            )
        }
    }
}

@Composable
@Preview
private fun ChatPreview() {
    Chat.View("channel.lobby")
}
