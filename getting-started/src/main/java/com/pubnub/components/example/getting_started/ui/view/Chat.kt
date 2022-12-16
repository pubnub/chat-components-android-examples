package com.pubnub.components.example.getting_started.ui.view


import android.os.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pubnub.components.chat.provider.LocalMessageRepository
import com.pubnub.components.chat.ui.component.input.MessageInput
import com.pubnub.components.chat.ui.component.member.MemberUi
import com.pubnub.components.chat.ui.component.message.MessageList
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.presence.Presence
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.data.message.DBMessage
import com.pubnub.components.example.getting_started.Settings
import com.pubnub.components.repository.message.DefaultMessageRepository
import com.pubnub.components.repository.message.MessageRepository
import com.pubnub.framework.data.ChannelId
import com.pubnub.framework.util.toTimetoken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

object Chat {

    @Composable
    internal fun Content(
        messages: Flow<PagingData<MessageUi>>,
        presence: Presence? = null,
        onMessageSelected: (MessageUi.Data) -> Unit = {},
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
                onMessageSelected = onMessageSelected,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, true),
            )

            MessageInput(
                typingIndicatorEnabled = true,
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    @Composable
    fun View(
        channelId: ChannelId,
    ) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.defaultWithMediator()
        val messages = remember(channelId) { messageViewModel.getAll(channelId) }
        // endregion

        CompositionLocalProvider(
            LocalChannel provides channelId
        ) {
            Content(
                messages = messages,
            )
        }

        val messageRepository = LocalMessageRepository.current as DefaultMessageRepository
        LaunchedEffect(channelId){
            send(messageRepository, fakeData(channelId, 1))
            send(messageRepository, fakeData(channelId, 2))
            delay(5_000L)

//            send(messageRepository, fakeData(channelId, 10))
//            send(messageRepository, fakeData(channelId, 9))
//            delay(5_000L)
//
//            send(messageRepository, fakeData(channelId, 8))
//            send(messageRepository, fakeData(channelId, 7))
//            delay(5_000L)

            send(messageRepository, fakeData(channelId, 6))
            send(messageRepository, fakeData(channelId, 5))
            delay(5_000L)

            send(messageRepository, fakeData(channelId, 4))
            send(messageRepository, fakeData(channelId, 3))
            delay(5_000L)
        }
    }
}

private suspend fun send(messageRepository: DefaultMessageRepository, message: MessageUi.Data){
    val dbMessage = DBMessage(
        id = message.uuid,
        text = message.text,
        contentType = message.contentType,
        content = message.content,
        custom = message.custom,
        publisher = message.publisher.id,
        published = message.published,
        channel = message.channel,
        timetoken = message.published,
        isSent = false,
        exception = null,
    )

    // Add message to repository
    messageRepository.insertOrUpdate(dbMessage)
}

private fun fakeData(channelId: ChannelId, i: Int) = MessageUi.Data("$i", MemberUi.Data(Settings.userId, ""), channelId, "$i", "$i", i.toLong(), i.toLong(), false, false, listOf(), "")


@Composable
@Preview
private fun ChatPreview() {
    Chat.View("channel.lobby")
}
