package com.pubnub.components.example.getting_started

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.chat.provider.LocalMemberRepository
import com.pubnub.components.chat.provider.LocalMembershipRepository
import com.pubnub.components.chat.ui.component.menu.Copy
import com.pubnub.components.chat.ui.component.menu.React
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.chat.viewmodel.message.ReactionViewModel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.ui.theme.AppTheme
import com.pubnub.components.example.getting_started.ui.view.Chat
import com.pubnub.components.example.getting_started.ui.view.Menu
import com.pubnub.framework.data.ChannelId
import kotlinx.coroutines.launch
import timber.log.Timber

class ChatActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    private val channel: ChannelId = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePubNub()
        setContent {
            AppTheme(pubNub = pubNub) {
                AddDummyData(channel)
                Box(modifier = Modifier.fillMaxSize()) {
                    Chat.View(channel)
                }
            }
        }
    }

    override fun onDestroy() {
        destroyPubNub()
        super.onDestroy()
    }

    private fun initializePubNub() {
        pubNub = PubNub(
            PNConfiguration(uuid = "myFirstUser").apply {
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.NONE
            }
        )
    }

    private fun destroyPubNub() {
        pubNub.destroy()
    }

    @Composable
    fun AddDummyData(vararg channelId: ChannelId) {

        // Creates a user object with uuid
        val memberRepository = LocalMemberRepository.current
        val member: DBMember = DBMember(
            id = pubNub.configuration.uuid,
            name = "myFirstUser",
            profileUrl = "https://picsum.photos/seed/${pubNub.configuration.uuid}/200"
        )

        // Creates a membership so that the user could subscribe to channels
        val membershipRepository = LocalMembershipRepository.current
        val memberships: Array<DBMembership> =
            channelId.map { id -> DBMembership(channelId = id, memberId = member.id) }
                .toTypedArray()

        // Fills the database with member and memberships data
        val scope = rememberCoroutineScope()
        LaunchedEffect(null) {
            scope.launch {
                memberRepository.insertOrUpdate(member)
                membershipRepository.insertOrUpdate(*memberships)
            }
        }
    }
}
