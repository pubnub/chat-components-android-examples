package com.pubnub.components.example.getting_started

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.chat.provider.LocalMemberRepository
import com.pubnub.components.chat.provider.LocalMembershipRepository
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.ui.component.message.reaction.PickedReaction
import com.pubnub.components.chat.ui.component.message.reaction.renderer.DefaultReactionsPickerRenderer
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.ui.component.provider.LocalPubNub
import com.pubnub.components.chat.viewmodel.message.MessageViewModel
import com.pubnub.components.chat.viewmodel.message.ReactionViewModel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.ui.theme.AppTheme
import com.pubnub.components.example.getting_started.ui.view.Chat
import com.pubnub.framework.data.ChannelId
import com.pubnub.framework.data.MessageId
import kotlinx.coroutines.launch
import timber.log.Timber

class ChatActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        initializePubNub()
        setContent {
            AppTheme(pubNub = pubNub) {
                AddDummyData()

                val channel: ChannelId = "Default"
                val reactionViewModel: ReactionViewModel = ReactionViewModel.default(channel)
                val coroutineScope = rememberCoroutineScope()

                DefaultReactionsPickerRenderer.ReactionsBottomSheetLayout(
                    sheetState = reactionViewModel.menuState,
                    onSelected = {
                        reactionViewModel.reactionSelected(
                            type = it.type,
                            value = it.value,
                        )
                        coroutineScope.launch { reactionViewModel.hideMenu() }
                    },
                ) {
                    Scaffold {
                        ChannelView(
                            id = channel,
                            showMenu = {
                                coroutineScope.launch { reactionViewModel.showMenu(it) }
                                       },
                            reactionSelected = reactionViewModel::reactionSelected,
                        )
                    }
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
    fun ChannelView(
        id: ChannelId,
        showMenu: ((MessageUi.Data) -> Unit)? = null,
        reactionSelected: ((PickedReaction) -> Unit)? = null
    ) {
        // region Content data
        val messageViewModel: MessageViewModel = MessageViewModel.defaultWithMediator(id)
        val messages = remember { messageViewModel.getAll() }
        // endregion

        val coroutineScope = rememberCoroutineScope()
        val onShowMenu: ((MessageId) -> Unit)? = showMenu?.let {
            { messageId ->
                coroutineScope.launch {
                    val message = messageViewModel.get(messageId)
                    message?.let {
                        showMenu(it)
                    }

                }
            }
        }
        CompositionLocalProvider(LocalChannel provides id) {
            Chat.Content(
                messages = messages,
                onShowMenu = onShowMenu,
                onReactionSelected = reactionSelected,
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
