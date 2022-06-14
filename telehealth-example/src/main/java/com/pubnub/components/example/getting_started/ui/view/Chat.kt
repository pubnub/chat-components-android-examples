package com.pubnub.components.example.getting_started.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.pubnub.components.chat.ui.component.common.ThemeDefaults
import com.pubnub.components.chat.ui.component.input.MessageInput
import com.pubnub.components.chat.ui.component.input.renderer.AnimatedTypingIndicatorRenderer
import com.pubnub.components.chat.ui.component.menu.Copy
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.LocalMessageListTheme
import com.pubnub.components.chat.ui.component.message.MessageList
import com.pubnub.components.chat.ui.component.message.MessageListTheme
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.presence.Presence
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.chat.viewmodel.message.ReactionViewModel
import com.pubnub.components.example.getting_started.R
import com.pubnub.framework.data.ChannelId
import kotlinx.coroutines.flow.Flow

object Chat {

    @OptIn(ExperimentalGraphicsApi::class)
    @Composable
    internal fun Content(
        messages: Flow<PagingData<MessageUi>>,
        presence: Presence? = null,
        onMessageSelected: (MessageUi.Data) -> Unit,
        onReactionSelected: ((React) -> Unit)? = null,
    ) {

        val customTheme = ThemeDefaults.messageList(modifier = Modifier.width(200.dp))
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
        MessageListTheme(customTheme) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.hsl(196F, 0.65F, 0.57F))){
                Image(modifier = Modifier
                    .size(46.dp)
                    .padding(top = 16.dp), painter = painterResource(id = R.drawable.chevron), contentDescription = "logo")
                Spacer(modifier = Modifier.width(80.dp))
                Column() {
                    Text(text = "Anna Gordon", modifier = Modifier.padding(top = 8.dp, start = 32.dp), color = Color.White, fontSize = 16.sp)
                    Text(text = "Patient ID: 98766", modifier = Modifier.padding(start = 20.dp, bottom = 10.dp), color = Color.White, fontSize = 16.sp)
                }
            }
            MessageList(
                messages = messages,
                presence = presence,
                onMessageSelected = onMessageSelected,
                onReactionSelected = onReactionSelected,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, true),
            )
        }
            MessageInput(
                typingIndicator = true,
                typingIndicatorRenderer = AnimatedTypingIndicatorRenderer,
            )
        }
    }

    @Composable
    fun MessageListTheme(
        theme: MessageListTheme,
        content: @Composable() () -> Unit,
    ) {
        CompositionLocalProvider(LocalMessageListTheme provides theme) {
            content()
        }
    }

    @Composable
    fun View(
        channelId: ChannelId,
    ) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.defaultWithMediator(channelId)
        val messages = remember { messageViewModel.getAll() }

        val reactionViewModel: ReactionViewModel = ReactionViewModel.default()
        // endregion

        var menuVisible by remember { mutableStateOf(false) }
        var selectedMessage by remember { mutableStateOf<MessageUi.Data?>(null) }

        CompositionLocalProvider(LocalChannel provides channelId) {
            Menu(
                visible = menuVisible,
                message = selectedMessage,
                onDismiss = { menuVisible = false },
                onAction = { action ->
                    when (action) {
                        is Copy -> {
                            action.message.text?.let { content ->
                                messageViewModel.copy(AnnotatedString(content))
                            }
                        }
                        is React -> reactionViewModel.reactionSelected(action)
                        else -> {}
                    }
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
