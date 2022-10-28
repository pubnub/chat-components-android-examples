package com.pubnub.components.example.telehealth.ui.view


import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.map
import com.pubnub.components.chat.ui.component.channel.ChannelList
import com.pubnub.components.chat.ui.component.channel.ChannelListTheme
import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.ui.component.common.ThemeDefaults
import com.pubnub.components.chat.ui.component.member.MemberUi
import com.pubnub.components.chat.viewmodel.channel.ChannelViewModel
import com.pubnub.components.example.getting_started.R
import com.pubnub.components.example.telehealth.ChatActivity
import com.pubnub.components.example.telehealth.clearFocusOnTap
import com.pubnub.components.example.telehealth.dto.Parameters
import com.pubnub.components.example.telehealth.dto.Parameters.Companion.PARAMETERS_BUNDLE_KEY
import com.pubnub.components.example.telehealth.ui.theme.ToolbarColor
import com.pubnub.components.example.telehealth.ui.theme.Typography
import kotlinx.coroutines.flow.Flow

object Channel {

    @Composable
    internal fun Content(
        channels: Flow<PagingData<ChannelUi>>,
        type: String,
        onSelected: (ChannelUi.Data) -> Unit = {},
    ) {
        val customTheme = ThemeDefaults.channelList()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clearFocusOnTap()
        ) {
            ChannelListTheme(customTheme) {
                ChannelList(
                    channels = channels,
                    onSelected = onSelected,
                    headerContent = {
                        headerTitle(type)
                    }
                )
            }
        }
    }

    @Composable
    fun headerTitle(type: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ToolbarColor)
        ) {
            val title = if (type == "patient") {
                stringResource(R.string.doctor_top_bar)
            } else {
                stringResource(R.string.patient_top_bar)
            }
            Text(
                text = title,
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 20.dp,
                    bottom = 16.dp
                ),
                style = Typography.body2
            )
            Spacer(modifier = Modifier.width(160.dp))
        }
    }

    @Composable
    fun View(
        parameters: Parameters,
    ) {
        // region Content data
        val channelViewModel: ChannelViewModel =
            ChannelViewModel.default(LocalContext.current.resources)
        val channels = remember {
            channelViewModel.getAll(transform = {
                map { channelUi: ChannelUi ->
                    channelUi as ChannelUi.Data
                    val otherMember: MemberUi.Data? =
                        channelUi.members.firstOrNull { it.id != parameters.userId }
                    ChannelUi.Data(
                        id = channelUi.id,
                        members = channelUi.members,
                        name = otherMember?.name ?: channelUi.name,
                        type = channelUi.type,
                        description = otherMember?.description ?: channelUi.description,
                        profileUrl = otherMember?.profileUrl ?: channelUi.profileUrl
                    )
                }
            }
            )
        }
        val context = LocalContext.current
        CompositionLocalProvider {
            Content(
                channels = channels,
                onSelected = {
                    val chatParameters = Parameters(
                        channelId = it.id,
                        secondUserId = it.description ?: "",
                        secondUserName = it.name,
                        userId = parameters.userId,
                        type = parameters.type
                    )
                    val intent = Intent(context, ChatActivity::class.java).apply {
                        putExtra(PARAMETERS_BUNDLE_KEY, chatParameters)
                    }
                    context.startActivity(intent)
                },
                type = parameters.type,
            )
        }
    }


}

