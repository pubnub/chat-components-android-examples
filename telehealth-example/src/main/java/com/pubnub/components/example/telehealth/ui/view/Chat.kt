package com.pubnub.components.example.telehealth.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.pubnub.components.chat.ui.component.common.ShapeThemeDefaults
import com.pubnub.components.chat.ui.component.common.TextThemeDefaults.text
import com.pubnub.components.chat.ui.component.common.ThemeDefaults
import com.pubnub.components.chat.ui.component.input.MessageInput
import com.pubnub.components.chat.ui.component.menu.Copy
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.MessageList
import com.pubnub.components.chat.ui.component.message.MessageListTheme
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.presence.Presence
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel.Companion.defaultWithMediator
import com.pubnub.components.chat.viewmodel.message.ReactionViewModel
import com.pubnub.components.example.getting_started.R
import com.pubnub.components.example.telehealth.ui.theme.ChatBackgroundColor
import com.pubnub.components.example.telehealth.ui.theme.MessageBackgroundColor
import com.pubnub.components.example.telehealth.ui.theme.MessageOwnBackgroundColor
import com.pubnub.components.example.telehealth.ui.theme.Shapes
import com.pubnub.framework.data.ChannelId
import kotlinx.coroutines.flow.Flow

object Chat {

    @Composable
    internal fun Content(
        messages: Flow<PagingData<MessageUi>>,
        onMessageSelected: (MessageUi.Data) -> Unit,
        patientId: String,
        patientName: String,
        presence: Presence? = null,
        onReactionSelected: ((React) -> Unit)? = null,
    ) {

        val customTheme = ThemeDefaults.messageList(
            messageOwn = ThemeDefaults.message(
                text = text(
                    Modifier
                        .background(color = MessageOwnBackgroundColor)
                        .padding(2.dp)

                ),
                shape = ShapeThemeDefaults.shape(shape = Shapes.medium),
                modifier = Modifier.fillMaxSize()
            ),
            message = ThemeDefaults.message(
                text = text(
                    modifier = Modifier
                        .background(color = MessageBackgroundColor)
                        .padding(2.dp)
                ),
                shape = ShapeThemeDefaults.shape(shape = Shapes.medium),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxSize()
            )
        )
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = ChatBackgroundColor)
                ) {
                    Image(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(top = 16.dp)
                            .clickable {

                            },
                        painter = painterResource(id = R.drawable.chevron),
                        contentDescription = stringResource(id = R.string.logo),
                    )
                    Spacer(modifier = Modifier.width(80.dp))
                    Column {
                        Text(
                            text = patientName,
                            modifier = Modifier.padding(top = 8.dp, start = 32.dp),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = patientId,
                            modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
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
                typingIndicatorEnabled = true,
            )
        }
    }

    @Composable
    fun View(
        channelId: ChannelId,
        patientId: String,
        patientName: String
    ) {
        val messageViewModel: MessageViewModel = defaultWithMediator()
        val messages = remember(channelId) { messageViewModel.getAll(channelId) }

        val reactionViewModel: ReactionViewModel = ReactionViewModel.default()
        DisposableEffect(channelId) {
            reactionViewModel.bind(channelId)
            onDispose {
                reactionViewModel.unbind()
            }
        }

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
                            action.message.text.let { content ->
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
                patientId = patientId,
                patientName = patientName
            )
        }
    }
}

@Composable
@Preview
private fun ChatPreview() {
    Chat.View("channel.lobby", "123456", "Example Name")
}