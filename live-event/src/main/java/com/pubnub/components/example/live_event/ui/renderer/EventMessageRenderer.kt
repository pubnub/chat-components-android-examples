package com.pubnub.components.example.live_event.ui.renderer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.placeholder.placeholder
import com.pubnub.components.chat.provider.PubNubPreview
import com.pubnub.components.chat.ui.component.member.ProfileImage
import com.pubnub.components.chat.ui.component.message.LocalMessageListTheme
import com.pubnub.components.chat.ui.component.message.messageFormatter
import com.pubnub.components.chat.ui.component.message.reaction.Reaction
import com.pubnub.components.chat.ui.component.message.reaction.ReactionUi
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer
import com.pubnub.components.chat.ui.component.message.reaction.renderer.ReactionsRenderer
import com.pubnub.components.chat.ui.component.message.renderer.GroupMessageRenderer
import com.pubnub.components.chat.ui.component.message.renderer.MessageRenderer
import com.pubnub.components.example.live_event.ui.util.InteractionHelper
import com.pubnub.framework.data.MessageId
import com.pubnub.framework.data.UserId
import com.pubnub.framework.util.Timetoken
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.*

@OptIn(
    ExperimentalFoundationApi::class,
    DelicateCoroutinesApi::class
)

object EventMessageRenderer : MessageRenderer {

    @Composable
    override fun Message(
        messageId: MessageId,
        currentUserId: UserId,
        userId: UserId,
        profileUrl: String,
        online: Boolean?,
        title: String,
        message: AnnotatedString?,
        timetoken: Timetoken,
        reactions: List<ReactionUi>,
        onMessageSelected: (() -> Unit)?,
        onReactionSelected: ((Reaction) -> Unit)?,
        reactionsPickerRenderer: ReactionsRenderer,
    ) {
        ChatMessage(
            currentUserId = currentUserId,
            userId = userId,
            profileUrl = profileUrl,
            online = online,
            title = title,
            message = message,
            timetoken = timetoken,
            reactions = reactions,
            onMessageSelected = onMessageSelected?.let { { it() } },
            onReactionSelected = onReactionSelected,
            reactionsPicker = reactionsPickerRenderer,
        )
    }

    @Composable
    override fun Placeholder() {
        MessagePlaceholder()
    }

    @Composable
    override fun Separator(text: String) {
    }

    @Composable
    fun ChatMessage(
        currentUserId: UserId,
        userId: UserId,
        profileUrl: String?,
        online: Boolean?,
        @Suppress("UNUSED_PARAMETER") title: String,
        message: AnnotatedString?,
        @Suppress("UNUSED_PARAMETER") timetoken: Timetoken,
        placeholder: Boolean = false,
        @Suppress("UNUSED_PARAMETER") reactions: List<ReactionUi> = emptyList(),
        onMessageSelected: (() -> Unit)? = null,
        @Suppress("UNUSED_PARAMETER") onReactionSelected: ((Reaction) -> Unit)? = null,
        @Suppress("UNUSED_PARAMETER") reactionsPicker: ReactionsRenderer = DefaultReactionsPickerRenderer,
    ) {
        val theme = if (currentUserId == userId) LocalMessageListTheme.current.message
        else LocalMessageListTheme.current.messageOwn

        // region Placeholders
        val messagePlaceholder = Modifier.placeholder(
            visible = placeholder,
            color = Color.LightGray,
            shape = theme.shape.shape,
        ).let { if (placeholder) it.fillMaxWidth(0.8f) else it }

        val imagePlaceholder = Modifier.placeholder(
            visible = placeholder,
            color = Color.LightGray,
            shape = CircleShape,
        )
        // endregion

        // Ripple region
        val interactionSource = remember { MutableInteractionSource() }
        val interactionHelper = remember { InteractionHelper(interactionSource) }
        // endregion

        val localFocusManager = LocalFocusManager.current

        Row(
            modifier = Modifier
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            localFocusManager.clearFocus()
                            interactionHelper.emitPressEvent(this, offset)
                        },
                        onLongPress = { offset ->
                            onMessageSelected?.let { onMessageSelected() }
                            interactionHelper.emitReleaseEvent(offset)
                        },
                    )
                }
                .then(theme.modifier),
            verticalAlignment = theme.verticalAlignment,
        ) {
            Box(modifier = theme.profileImage.modifier) {
                ProfileImage(
                    modifier = imagePlaceholder,
                    imageUrl = profileUrl,
                    isOnline = online,
                )
            }

            Column {
                val body: @Composable() () -> Unit = {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {

                        if (message != null && message.isNotBlank()) {
                            GroupMessageRenderer.ChatText(
                                message = message,
                                theme = theme.text,
                                placeholderModifier = theme.text.modifier.then(
                                    messagePlaceholder.then(
                                        Modifier.padding(
                                            theme.shape.padding
                                        )
                                    )
                                ),
                                modifier = theme.text.modifier,
                                onLongPress = { offset ->
                                    onMessageSelected?.let { onMessageSelected() }
                                    interactionHelper.emitReleaseEvent(offset)
                                },
                                onPress = { offset ->
                                    localFocusManager.clearFocus()
                                    interactionHelper.emitPressEvent(this, offset)
                                }
                            )
                        }
                    }
                }

                // workaround for placeholders...
                if (placeholder) body()
                else Surface(
                    color = theme.shape.tint,
                    shape = theme.shape.shape,
                    modifier = theme.shape.modifier
                ) { body() }
            }
        }
    }

    @Composable
    fun MessagePlaceholder(
    ) {
        ChatMessage(
            currentUserId = "a",
            userId = "b",
            profileUrl = "",
            online = false,
            title = "Lorem ipsum dolor",
            message = messageFormatter(text = "Test message"),
            timetoken = 0L,
            placeholder = true,
            reactionsPicker = DefaultReactionsPickerRenderer,
        )
    }
}

@Preview
@Composable
private fun MessagePreview(
) {
    PubNubPreview {
        EventMessageRenderer.ChatMessage(
            currentUserId = "a",
            userId = "b",
            profileUrl = "",
            online = false,
            title = "Lorem ipsum dolor",
            message = messageFormatter(text = "Test message"),
            timetoken = 0L,
            placeholder = true,
            reactionsPicker = DefaultReactionsPickerRenderer,
        )
    }
}