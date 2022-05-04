package com.pubnub.components.example.getting_started.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.pubnub.components.chat.ui.component.menu.BottomMenu
import com.pubnub.components.chat.ui.component.menu.MenuAction
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Menu(
    visible: Boolean,
    message: MessageUi.Data?,
    onAction: (MenuAction) -> Unit,
    onDismiss: () -> Unit,
) {
    BottomMenu(
        visible = visible && message != null,
        headerContent = {
            DefaultReactionsPickerRenderer.ReactionsPicker { reaction ->
                onAction(React(reaction, message!!))
                onDismiss()
            }
        },
        onAction = { action ->
            onAction(action)
            onDismiss()
        },
        message = message,
        onDismiss = onDismiss,
    )
}
