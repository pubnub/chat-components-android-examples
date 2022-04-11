package com.pubnub.components.example.getting_started

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.chat.provider.LocalMemberRepository
import com.pubnub.components.chat.provider.LocalMembershipRepository
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.repository.member.DefaultMemberRepository
import com.pubnub.components.repository.membership.DefaultMembershipRepository
import com.pubnub.components.example.getting_started.ui.theme.AppTheme
import com.pubnub.components.example.getting_started.ui.view.Chat
import com.pubnub.framework.data.ChannelId
import kotlinx.coroutines.launch

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
                    ChannelView(id = channel)
                }
            }
        }
    }

    override fun onDestroy() {
        destroyPubNub()
        super.onDestroy()
    }

    private fun initializePubNub(){
      pubNub  = PubNub(
            PNConfiguration(uuid = "myFirstUser").apply {
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.NONE
            }
        )
    }

    private fun destroyPubNub(){
        pubNub.destroy()
    }

    // Channel view
    @Composable
    fun ChannelView(id: ChannelId) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.defaultWithMediator(id)
        val messages = remember { messageViewModel.getAll() }
        // endregion

        CompositionLocalProvider(LocalChannel provides id) {
            Chat.Content(
                messages = messages,
            )
        }
    }

    @Composable
    fun AddDummyData(vararg channelId: ChannelId) {

        // Creates a user object with uuid
        val memberRepository: DefaultMemberRepository = LocalMemberRepository.current
        val member: DBMember = DBMember(id = pubNub.configuration.uuid, name = "myFirstUser", profileUrl = "https://picsum.photos/seed/${pubNub.configuration.uuid}/200")

        // Creates a membership so that the user could subscribe to channels
        val membershipRepository: DefaultMembershipRepository = LocalMembershipRepository.current
        val memberships: Array<DBMembership> = channelId.map { id -> DBMembership(channelId = id, memberId = member.id) }.toTypedArray()

        // Fills the database with member and memberships data
        val scope = rememberCoroutineScope()
        LaunchedEffect(null) {
            scope.launch {
                memberRepository.add(member)
                membershipRepository.add(*memberships)
            }
        }
    }
}
