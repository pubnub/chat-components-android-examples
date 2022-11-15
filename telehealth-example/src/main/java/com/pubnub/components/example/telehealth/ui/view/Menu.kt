package com.pubnub.components.example.telehealth.ui.view

import androidx.compose.runtime.Composable
import com.pubnub.components.chat.ui.component.menu.BottomMenu
import com.pubnub.components.chat.ui.component.menu.MenuAction
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer

@Composable
fun Menu(
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
        headerContent = {
            DefaultReactionsPickerRenderer.ReactionsPicker { reaction ->
                message?.let { onAction(React(reaction, message)) }
            }
        },
    )
}
