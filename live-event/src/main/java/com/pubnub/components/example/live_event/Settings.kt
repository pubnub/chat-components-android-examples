package com.pubnub.components.example.live_event

import com.pubnub.framework.data.ChannelId
import com.pubnub.framework.data.UserId
import io.github.serpro69.kfaker.Faker

object Settings {
    private val faker: Faker = Faker()
    const val channelId: ChannelId = "demo"
    val userId: UserId = faker.name.name().replace(' ', '_')
}
