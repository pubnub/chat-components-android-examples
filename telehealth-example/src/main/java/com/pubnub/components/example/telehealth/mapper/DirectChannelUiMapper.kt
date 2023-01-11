package com.pubnub.components.example.telehealth.mapper

import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.ui.component.member.MemberUi
import com.pubnub.components.chat.ui.mapper.channel.DBChannelMapper
import com.pubnub.components.data.channel.DBChannelWithMembers
import com.pubnub.framework.mapper.Mapper

class DirectChannelUiMapper(
    private val userId: String,
    private val mapper: Mapper<DBChannelWithMembers, ChannelUi.Data> = DBChannelMapper(),
): Mapper<DBChannelWithMembers, ChannelUi.Data> {
    override fun map(input: DBChannelWithMembers): ChannelUi.Data {
        val base = mapper.map(input)
        val otherMember: MemberUi.Data? =
            base.members.firstOrNull { it.id != userId }
        return base.copy(
            name = otherMember?.name ?: base.name,
            description = otherMember?.description ?: base.description,
            profileUrl = otherMember?.profileUrl ?: base.profileUrl
        )
    }
}
