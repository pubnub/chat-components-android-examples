package com.pubnub.components.example.telehealth.dto

import android.content.Intent
import android.os.Parcelable
import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.example.telehealth.ui.view.Channel
import kotlinx.parcelize.Parcelize

@Suppress("DEPRECATION")
@Parcelize
data class ChatParameters(
    val userId: String,
    val type: String,
    val channelId: String,
    val secondUserId: String,
    val secondUserName: String
) : Parcelable {
    companion object {
        const val PARAMETERS_BUNDLE_KEY = "PARAMETERS_BUNDLE_KEY"

        fun fromIntent(intent: Intent): ChatParameters? =
            intent.extras?.getParcelable<ChatParameters>(PARAMETERS_BUNDLE_KEY)
    }
}