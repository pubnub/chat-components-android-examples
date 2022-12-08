package com.pubnub.components.example.telehealth.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.map
import com.pubnub.components.chat.ui.component.channel.ChannelList
import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.viewmodel.channel.ChannelViewModel
import com.pubnub.components.example.telehealth.clearFocusOnTap
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.dto.UserParameters
import com.pubnub.components.example.telehealth.mapper.ChannelUiMapper
import com.pubnub.components.example.telehealth.ui.theme.ChatBackgroundColor
import com.pubnub.components.example.telehealth.ui.theme.Typography
import com.pubnub.components.example.telehealth_example.R
import kotlinx.coroutines.flow.Flow

object Channel {

    @Composable
    internal fun Content(
        channels: Flow<PagingData<ChannelUi>>,
        type: String,
        onSelected: (ChannelUi.Data) -> Unit = {},
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap()) {
            ChannelList(channels = channels, onSelected = onSelected, headerContent = {
                HeaderTitle(type)
            })
        }
    }

    @Composable
    fun HeaderTitle(type: String) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = ChatBackgroundColor)) {
            val title = if (type == "patient") {
                stringResource(R.string.doctor_top_bar)
            } else {
                stringResource(R.string.patient_top_bar)
            }
            Text(text = title,
                modifier = Modifier.padding(top = 16.dp, start = 20.dp, bottom = 16.dp),
                style = Typography.body2)
        }
    }

    @Composable
    fun View(
        userParameters: UserParameters,
        onChannelSelected: (ChatParameters) -> Unit = {},
    ) {
        // region Content data
        val channelUiMapper = ChannelUiMapper()
        val channelViewModel: ChannelViewModel =
            ChannelViewModel.default(LocalContext.current.resources)
        val channels = remember {
            channelViewModel.getAll(transform = {
                map { channelUi: ChannelUi ->
                    channelUiMapper.map(channelUi, userParameters.userId)
                }
            })
        }
        CompositionLocalProvider {
            Content(
                channels = channels,
                onSelected = {
                    onChannelSelected(ChatParameters(
                        userId = userParameters.userId,
                        type = userParameters.type,
                        channelId = it.id,
                        secondUserId = it.description ?: "",
                        secondUserName = it.name,
                    ))
                },
                type = userParameters.type,
            )
        }
    }
}

