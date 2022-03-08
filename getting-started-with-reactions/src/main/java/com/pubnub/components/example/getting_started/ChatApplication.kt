package com.pubnub.components.example.getting_started

import android.app.Application
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer
import com.pubnub.components.data.Database

class ChatApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Database.initialize(this)

//                        DefaultReactionsPickerRenderer.emojis = listOf(
//                    UnicodeEmoji("\uD83D\uDE4A"),    // 🙊
//                    UnicodeEmoji("\uD83D\uDE49"),    // 🙉
//                    UnicodeEmoji("\uD83D\uDE48"),    // 🙈
//                    UnicodeEmoji("\uD83D\uDC12"),    // 🐒
//                    UnicodeEmoji("\uD83E\uDD8D"),    // 🦍
//                    UnicodeEmoji("\uD83E\uDD84"),    // 🦄
//                )
//                DefaultReactionsPickerRenderer.visibleItemsCount = 4
    }
}
