package com.pubnub.components.example.getting_started.ui.view

import androidx.compose.runtime.Composable
import com.pubnub.components.chat.ui.component.menu.BottomMenu
import com.pubnub.components.chat.ui.component.menu.MenuAction
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer

@Composable
fun Login(
    visible: Boolean,
    message: MessageUi.Data?,
    onAction: (MenuAction) -> Unit,
    onDismiss: () -> Unit,
) {
    BottomMenu(
        visible = visible && message != null,
        onAction = { action ->
            onAction(action)
            onDismiss()
        },
        message = message,
        onDismiss = onDismiss,
    )
}
