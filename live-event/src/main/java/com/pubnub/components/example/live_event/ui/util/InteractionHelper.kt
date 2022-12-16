package com.pubnub.components.example.live_event.ui.util

import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class InteractionHelper(
    private val interactionSource: MutableInteractionSource,
    private val coroutineScope: CoroutineScope = GlobalScope,
) {

    suspend fun emitPressEvent(scope: PressGestureScope, offset: Offset) {
        val press = PressInteraction.Press(offset)
        interactionSource.emit(press)
        scope.tryAwaitRelease()
        interactionSource.emit(PressInteraction.Release(press))
    }

    fun emitReleaseEvent(offset: Offset) {
        coroutineScope.launch {
            interactionSource.emit(
                PressInteraction.Release(
                    PressInteraction.Press(offset)
                )
            )
        }
    }
}
