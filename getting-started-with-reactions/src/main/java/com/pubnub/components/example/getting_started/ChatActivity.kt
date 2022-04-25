package com.pubnub.components.example.getting_started

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
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

    // Channel view
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ChannelView(id: ChannelId) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.defaultWithMediator(id)
        val messages = remember { messageViewModel.getAll() }

        val reactionViewModel: ReactionViewModel = ReactionViewModel.default()
        // endregion

        var menuVisible by remember { mutableStateOf(false) }
        var selectedMessage by remember { mutableStateOf<MessageUi.Data?>(null) }

        CompositionLocalProvider(LocalChannel provides id) {

            Menu(
                visible = menuVisible,
                message = selectedMessage,
                onDismiss = { menuVisible = false },
                onAction = { action ->
                    when (action) {
                        is Copy -> {
                            action.message.text?.let { content ->
                                messageViewModel.copy(AnnotatedString(content))
                            }
                        }
                        is React -> reactionViewModel.reactionSelected(action)
                        else -> {}
                    }
                }
            )


            Chat.Content(
                messages = messages,
                onMessageSelected = {
                    selectedMessage = it
                    menuVisible = true
                },
                onReactionSelected = reactionViewModel::reactionSelected,
            )
        }
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
