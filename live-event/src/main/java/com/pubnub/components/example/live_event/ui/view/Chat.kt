package com.pubnub.components.example.live_event.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.pubnub.components.chat.service.channel.LocalOccupancyService
import com.pubnub.components.chat.ui.component.common.*
import com.pubnub.components.chat.ui.component.input.LocalMessageInputTheme
import com.pubnub.components.chat.ui.component.input.MessageInput
import com.pubnub.components.chat.ui.component.menu.Copy
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.LocalMessageListTheme
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.presence.Presence
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.example.live_event.R
import com.pubnub.framework.data.ChannelId
import com.pubnub.framework.data.Occupancy
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.*

object Chat {

    @Composable
    internal fun Content(
        messages: Flow<PagingData<MessageUi>>,
        occupancy: Occupancy,
        presence: Presence? = null,
        onMessageSelected: (MessageUi.Data) -> Unit,
        onReactionSelected: ((React) -> Unit)? = null,
    ) {
        val localFocusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .imePadding()
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                }
        ) {
            VideoContent()
            Header(occupancy.occupancy)
            com.pubnub.components.example.live_event.ui.renderer.MessageList(
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
                placeholder = stringResource(id = R.string.type_your_message)
            )
        }
    }

    @Composable
    fun View(
        channelId: ChannelId,
    ) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.default()
        val messages = remember(channelId) { messageViewModel.getAll(channelId) }
        // endregion

        // should be moved to VM
        val occupancyService = LocalOccupancyService.current
        val occupancy by occupancyService.getOccupancy(channelId).collectAsState(
            initial = Occupancy(
                channelId,
                0
            )
        )

        Timber.e("Occupancy: ${occupancy.occupancy}")
        var menuVisible by remember { mutableStateOf(false) }
        var selectedMessage by remember { mutableStateOf<MessageUi.Data?>(null) }

        val onDismiss: () -> Unit = { menuVisible = false }
        CompositionLocalProvider(
            LocalChannel provides channelId,
            LocalMessageListTheme provides EventMessageListTheme,
            LocalMessageInputTheme provides EventMessageInputTheme,
        ) {
            Menu(
                visible = menuVisible,
                message = selectedMessage,
                onDismiss = onDismiss,
                onAction = { action ->
                    when (action) {
                        is Copy -> {
                            messageViewModel.copy(AnnotatedString(action.message.text))
                        }
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
                occupancy = occupancy,
            )
        }
    }

    @Composable
    fun Header(occupancy: Int? = null) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(12.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Text(
                text = "Stream Chat".uppercase(Locale.getDefault()),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700,
                    color = MaterialTheme.colors.onSurface,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )
            if(occupancy != null){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = occupancy.toString(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W700,
                            color = MaterialTheme.colors.onSurface,
                        ),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(painter = rememberVectorPainter(image = Icons.Rounded.PeopleAlt),
                        contentDescription = stringResource(id = R.string.presence_icon),
                        tint = MaterialTheme.colors.onSurface,
                    )
                }
            }
        }
    }

    private val EventMessageListTheme
        @Composable get() =
            ThemeDefaults.messageList(
                message = EventMessageTheme,
                messageOwn = EventMessageTheme,
                arrangement = Arrangement.spacedBy(4.dp),
            )


    private val EventMessageTheme
        @Composable get() =
            ThemeDefaults.message(
                shape = ShapeThemeDefaults.messageBackground(
                    color = Color.Transparent,
                    padding = PaddingValues(2.dp),
                ),
                profileImage = ThemeDefaults.profileImage(
                    modifier = Modifier
                        .padding(8.dp, 4.dp, 4.dp, 4.dp)
                        .size(16.dp),
                ),
                modifier = Modifier.fillMaxWidth(),
            )


    private val EventMessageInputTheme
        @Composable get() =
            ThemeDefaults.messageInput(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                button = ButtonThemeDefaults.button(
                    text = TextThemeDefaults.text(
                        fontSize = 12.sp,
                    ),
                    // TODO: replace the icon after Components fix
                ),
                input = InputThemeDefaults.input(
                    // TODO: override the textStyle after Components fix
                )
            )
}


@Composable
@Preview
private fun ChatPreview() {
    Chat.View("channel.lobby")
}
