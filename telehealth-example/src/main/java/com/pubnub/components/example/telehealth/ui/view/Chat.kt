package com.pubnub.components.example.telehealth.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
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
import com.pubnub.components.example.telehealth.clearFocusOnTap
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.dto.Patient
import com.pubnub.components.example.telehealth.ui.theme.ChatBackgroundColor
import com.pubnub.components.example.telehealth.ui.theme.ChatMessageTheme
import com.pubnub.components.example.telehealth.ui.theme.Typography
import com.pubnub.components.example.telehealth_example.R
import kotlinx.coroutines.flow.Flow

object Chat {

    @Composable
    internal fun Content(
        messages: Flow<PagingData<MessageUi>>,
        onMessageSelected: (MessageUi.Data) -> Unit,
        patient: Patient,
        presence: Presence? = null,
        onReactionSelected: ((React) -> Unit)? = null,
        navController: NavHostController,
    ) {
        val customTheme = ChatMessageTheme()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clearFocusOnTap()
        ) {
            MessageListTheme(customTheme) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = ChatBackgroundColor),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Image(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(top = 8.dp, bottom = 8.dp)
                            .clickable {
                                navController.popBackStack()
                            },
                        painter = painterResource(id = R.drawable.chevron),
                        contentDescription = stringResource(id = R.string.logo),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = patient.name,
                            style = Typography.body2
                        )
                        Text(
                            text = patient.id,
                            style = Typography.body2
                        )
                    }
                    Spacer(modifier = Modifier
                        .size(36.dp)
                        .padding(16.dp))
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
        chatParameters: ChatParameters,
        navController: NavHostController,
    ) {
        val messageViewModel: MessageViewModel = defaultWithMediator()
        val messages =
            remember(chatParameters.channelId) { messageViewModel.getAll(chatParameters.channelId) }

        val reactionViewModel: ReactionViewModel = ReactionViewModel.default()
        DisposableEffect(chatParameters.channelId) {
            reactionViewModel.bind(chatParameters.channelId)
            onDispose {
                reactionViewModel.unbind()
            }
        }

        var menuVisible by remember { mutableStateOf(false) }
        var selectedMessage by remember { mutableStateOf<MessageUi.Data?>(null) }

        val onDismiss: () -> Unit = { menuVisible = false }
        CompositionLocalProvider(LocalChannel provides chatParameters.channelId) {
            Menu(
                visible = menuVisible,
                message = selectedMessage,
                onDismiss = onDismiss,
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
                patient = Patient(chatParameters.secondUserId, chatParameters.secondUserName),
                navController = navController
            )
        }
    }
}

@Composable
@Preview
private fun ChatPreview() {
    Chat.View(
        chatParameters = ChatParameters(
            userId = "123456",
            userType = "patient",
            channelId = "channel",
            secondUserId = "654321",
            secondUserName = "Example Name"
        ),
        navController = rememberNavController()
    )
}
