package com.pubnub.components.example.getting_started.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pubnub.components.chat.ui.component.menu.BottomMenu
import com.pubnub.components.chat.ui.component.menu.MenuAction
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BoxScope.Menu(
    visible: Boolean,
    message: MessageUi.Data?,
    onAction: (MenuAction) -> Unit,
    onDismiss: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible && message != null,
    ) {
        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .animateEnterExit(
                    enter = slideInVertically(
                        animationSpec = tween(),
                        initialOffsetY = { it },
                    ),
                    exit = slideOutVertically(
                        animationSpec = tween(),
                        targetOffsetY = { it },
                    )
                ),
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
            message = message!!,
            onDismiss = onDismiss,
        )
    }
}
