package com.pubnub.components.example.getting_started

import com.pubnub.framework.data.ChannelId
import com.pubnub.framework.data.UserId

object Settings {
    const val channelId: ChannelId = "Default"
    const val userId: UserId = "myFirstUser"
    val members = arrayOf("myFirstUser", "mySecondUser")
}
