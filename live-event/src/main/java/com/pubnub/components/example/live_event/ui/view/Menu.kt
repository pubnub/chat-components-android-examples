package com.pubnub.components.example.live_event.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pubnub.components.chat.ui.component.menu.BottomMenu
import com.pubnub.components.chat.ui.component.menu.MenuAction
import com.pubnub.components.chat.ui.component.message.MessageUi

@Composable
fun Menu(
    visible: Boolean,
    message: MessageUi.Data?,
    onAction: (MenuAction) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomMenu(
        message = message,
        headerContent = {},
        onAction = { action ->
            onAction(action)
        },
        onDismiss = onDismiss,
        visible = visible && message != null,
        modifier = modifier,
    )
}
