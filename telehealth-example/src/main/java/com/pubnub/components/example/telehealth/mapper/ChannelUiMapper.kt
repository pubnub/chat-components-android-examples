package com.pubnub.components.example.telehealth.mapper

import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.ui.component.member.MemberUi

class ChannelUiMapper {
    fun map(input: ChannelUi, userId: String): ChannelUi {
        input as ChannelUi.Data
        val otherMember: MemberUi.Data? =
            input.members.firstOrNull { it.id != userId }
        return input.copy(name = otherMember?.name ?: input.name,
            description = otherMember?.description ?: input.description,
            profileUrl = otherMember?.profileUrl ?: input.profileUrl)
    }
}