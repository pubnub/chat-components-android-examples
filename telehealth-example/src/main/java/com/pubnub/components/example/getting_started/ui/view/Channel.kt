package com.pubnub.components.example.getting_started.ui.view


import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.map
import com.pubnub.components.chat.ui.component.channel.ChannelList
import com.pubnub.components.chat.ui.component.channel.ChannelListTheme
import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.ui.component.channel.LocalChannelListTheme
import com.pubnub.components.chat.ui.component.common.ThemeDefaults
import com.pubnub.components.chat.ui.component.message.MessageUi
import com.pubnub.components.chat.viewmodel.channel.ChannelViewModel
import com.pubnub.components.example.getting_started.ChatActivity
import com.pubnub.components.example.getting_started.R
import kotlinx.coroutines.flow.Flow

object Channel {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    internal fun Content(
        channels: Flow<PagingData<ChannelUi>>,
        onSelected: (ChannelUi.Data) -> Unit = {},
        type: String?,
    ) {
        val customTheme = ThemeDefaults.channelList()
        val localFocusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                }
        ) {
            ChannelListTheme(customTheme) {
                ChannelList(
                    channels = channels,
                    onSelected = onSelected,
                    headerContent = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.hsl(196F, 0.65F, 0.57F))
                        ) {
                            val title = if (type == "patient"){
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
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(160.dp))
                        }
                    },
                )
            }
        }
    }

    @Composable
    fun ChannelListTheme(
        theme: ChannelListTheme,
        content: @Composable() () -> Unit,
    ) {
        CompositionLocalProvider(LocalChannelListTheme provides theme) {
            content()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun View(
        context: Context,
        uuid: String?,
        type: String?,
    ) {
        // region Content data
        val channelViewModel: ChannelViewModel = ChannelViewModel.default(context.resources)
        val channels = remember { channelViewModel.getAll() }

        CompositionLocalProvider {
            Content(
                channels = channels,
                onSelected = {
                    val intent = Intent(context, ChatActivity::class.java).apply {
                        putExtra("channelId", it.id)
                        putExtra("patientUuid", it.description)
                        putExtra("patientName", it.name)
                        putExtra("uuid", uuid)
                    }
                    context.startActivity(intent)
                },
                type = type,
            )
        }
    }


}

