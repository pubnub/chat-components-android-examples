package com.pubnub.components.example.live_event.ui.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.LocalMessageListTheme
import com.pubnub.components.chat.ui.component.message.MessageListContent
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer
import com.pubnub.components.chat.ui.component.presence.Presence
import com.pubnub.components.chat.ui.component.provider.LocalUser
import com.pubnub.components.example.live_event.R
import com.pubnub.framework.data.UserId
import kotlinx.coroutines.flow.Flow

@Composable
fun MessageList(
    messages: Flow<PagingData<MessageUi>>,
    modifier: Modifier = Modifier,
    presence: Presence? = null,
    onMessageSelected: ((MessageUi.Data) -> Unit)? = null,
    onReactionSelected: ((React) -> Unit)? = null,
    useStickyHeader: Boolean = false,
    headerContent: @Composable LazyItemScope.() -> Unit = {},
    footerContent: @Composable LazyItemScope.() -> Unit = {},
    itemContent: @Composable LazyListScope.(MessageUi?, UserId) -> Unit = { message, currentUser ->
        MessageListContent(
            message,
            currentUser,
            EventMessageRenderer,
            DefaultReactionsPickerRenderer,
            useStickyHeader,
            presence,
            onMessageSelected,
            onReactionSelected
        )
    },
) {

    val theme = LocalMessageListTheme.current
    val context = LocalContext.current
    val currentUser = LocalUser.current

    val lazyMessages: LazyPagingItems<MessageUi> = messages.collectAsLazyPagingItems()

    Box(modifier = modifier) {
        val lazyListState =
            rememberLazyListState(initialFirstVisibleItemIndex = lazyMessages.itemCount)

        LazyColumn(
            state = lazyListState,
            reverseLayout = true,
            verticalArrangement = theme.arrangement,
            modifier = theme.modifier.semantics {
                contentDescription = context.getString(R.string.message_list)
            }) {
            item {
                headerContent()
            }
            items(lazyMessages) { message ->
                this@LazyColumn.itemContent(message, currentUser)
            }
            item {
                footerContent()
            }
        }
    }
}
