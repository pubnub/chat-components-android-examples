package com.pubnub.components.example.telehealth

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.pubnub.api.PubNub
import com.pubnub.components.PubNubDatabase
import com.pubnub.components.asPubNub
import com.pubnub.components.chat.provider.*
import com.pubnub.components.chat.ui.component.channel.DefaultChannelListTheme
import com.pubnub.components.chat.ui.component.channel.LocalChannelListTheme
import com.pubnub.components.chat.ui.component.input.DefaultLocalMessageInputTheme
import com.pubnub.components.chat.ui.component.input.DefaultTypingIndicatorTheme
import com.pubnub.components.chat.ui.component.input.LocalMessageInputTheme
import com.pubnub.components.chat.ui.component.input.LocalTypingIndicatorTheme
import com.pubnub.components.chat.ui.component.member.DefaultMemberListTheme
import com.pubnub.components.chat.ui.component.member.LocalMemberListTheme
import com.pubnub.components.chat.ui.component.member.MemberUi
import com.pubnub.components.chat.ui.component.menu.DefaultMenuItemTheme
import com.pubnub.components.chat.ui.component.menu.LocalMenuItemTheme
import com.pubnub.components.chat.ui.component.message.*
import com.pubnub.components.chat.ui.component.message.reaction.DefaultReactionTheme
import com.pubnub.components.chat.ui.component.message.reaction.LocalReactionTheme
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.ui.component.provider.LocalPubNub
import com.pubnub.components.chat.ui.component.provider.LocalUser
import com.pubnub.components.chat.ui.mapper.member.DBMemberMapper
import com.pubnub.components.data.Database
import com.pubnub.components.data.channel.ChannelDao
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.channel.DBChannelWithMembers
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.components.data.member.MemberDao
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.data.membership.MembershipDao
import com.pubnub.components.data.message.DBMessage
import com.pubnub.components.data.message.MessageDao
import com.pubnub.components.data.message.action.DBMessageAction
import com.pubnub.components.data.message.action.DBMessageWithActions
import com.pubnub.components.data.message.action.MessageActionDao
import com.pubnub.components.example.getting_started.R
import com.pubnub.components.repository.channel.DefaultChannelRepository
import com.pubnub.components.repository.member.DefaultMemberRepository
import com.pubnub.components.repository.membership.DefaultMembershipRepository
import com.pubnub.components.repository.message.DefaultMessageRepository
import com.pubnub.components.repository.message.action.DefaultMessageActionRepository
import com.pubnub.framework.data.ChannelId
import com.pubnub.framework.data.UserId
import kotlinx.coroutines.runBlocking


@Composable
fun ChatProvider(
    pubNub: PubNub,
    database: PubNubDatabase<MessageDao<DBMessage, DBMessageWithActions>, MessageActionDao<DBMessageAction>, ChannelDao<DBChannel, DBChannelWithMembers>, MemberDao<DBMember, DBMemberWithChannels>, MembershipDao<DBMembership>> = Database.initialize(
        LocalContext.current
    ).asPubNub(),
    channel: ChannelId = "channel.lobby",
    synchronize: Boolean = true,
    content: @Composable() () -> Unit,
) {
    // Set PNSDK suffix
    @Suppress("DEPRECATION")
    pubNub.configuration.addPnsdkSuffix(getComponentsSuffix())

    RepositoryProvider(database) {
        CompositionLocalProvider(
            LocalPubNub providesDefault pubNub,
            LocalUser providesDefault pubNub.configuration.userId.value,
            LocalChannel providesDefault channel,

            // RTL support by locale
            LocalLayoutDirection providesDefault
                    if (LocalConfiguration.current.layoutDirection == View.LAYOUT_DIRECTION_RTL)
                        LayoutDirection.Rtl
                    else LayoutDirection.Ltr,

            // Themes
            LocalMessageInputTheme providesDefault DefaultLocalMessageInputTheme,
            LocalTypingIndicatorTheme providesDefault DefaultTypingIndicatorTheme,
            LocalChannelListTheme providesDefault DefaultChannelListTheme,
            LocalMemberListTheme providesDefault DefaultMemberListTheme,
            LocalMessageListTheme providesDefault DefaultMessageListTheme,
            LocalMessageTheme providesDefault DefaultLocalMessageTheme,
            LocalReactionTheme providesDefault DefaultReactionTheme,
            LocalIndicatorTheme providesDefault DefaultIndicatorTheme,
            LocalProfileImageTheme providesDefault DefaultProfileImageTheme,
            LocalMenuItemTheme providesDefault DefaultMenuItemTheme,
        ) {
            WithServices(synchronize) {
                content()
            }
        }
    }
}


@Composable
fun RepositoryProvider(
    database: PubNubDatabase<MessageDao<DBMessage, DBMessageWithActions>, MessageActionDao<DBMessageAction>, ChannelDao<DBChannel, DBChannelWithMembers>, MemberDao<DBMember, DBMemberWithChannels>, MembershipDao<DBMembership>> = Database.initialize(LocalContext.current).asPubNub(),
    content: @Composable() () -> Unit,
){
    // region Member part
    val unknownMemberTitle = stringResource(id = R.string.member_unknown_title)
    val unknownMemberDescription = stringResource(id = R.string.member_unknown_description)

    val memberDbMapper = DBMemberMapper()
    val memberRepository =
        DefaultMemberRepository(database.memberDao())
    val memberFormatter: (UserId) -> MemberUi.Data = { id ->
        runBlocking {
            (memberRepository.get(id)?.let { memberDbMapper.map(it) } ?: MemberUi.Data(
                id,
                unknownMemberTitle,
                null,
                unknownMemberDescription,
            ))
        }
    }
    // endregion

    // region Membership
    val membershipRepository = DefaultMembershipRepository(database.membershipDao())
    // endregion

    // region Channel
    val channelRepository = DefaultChannelRepository(database.channelDao())
    // endregion

    // region Message
    val messageRepository =
        DefaultMessageRepository(database.messageDao())
    val messageActionRepository = DefaultMessageActionRepository(database.actionDao())
    // endregion

    CompositionLocalProvider(
        // Repositories
        LocalChannelRepository providesDefault channelRepository,
        LocalMessageRepository providesDefault messageRepository,
        LocalMessageActionRepository providesDefault messageActionRepository,
        LocalMemberRepository providesDefault memberRepository,
        LocalMembershipRepository providesDefault membershipRepository,

        // Utils
        LocalMemberFormatter providesDefault memberFormatter,
    ) {
        content()
    }
}