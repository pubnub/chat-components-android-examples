package com.pubnub.components.example.getting_started.ui.view


import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.chat.ui.component.channel.ChannelList
import com.pubnub.components.chat.ui.component.channel.ChannelListTheme
import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.ui.component.channel.LocalChannelListTheme
import com.pubnub.components.chat.ui.component.common.TextTheme
import com.pubnub.components.chat.ui.component.common.ThemeDefaults
import com.pubnub.components.chat.ui.component.message.LocalMessageListTheme
import com.pubnub.components.chat.ui.component.message.MessageListTheme
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.channel.ChannelViewModel
import com.pubnub.components.data.Database
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.DefaultDataRepository
import com.pubnub.components.example.getting_started.R
import com.pubnub.components.example.getting_started.dto.ChannelsItem
import com.pubnub.components.example.getting_started.dto.MembershipsItem
import com.pubnub.framework.data.ChannelId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException

object Channel {

    @OptIn(ExperimentalGraphicsApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    internal fun Content(
        channels: Flow<PagingData<ChannelUi>>,
        onSelected: (ChannelUi.Data) -> Unit = {},
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
                    header = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.hsl(196F, 0.65F, 0.57F))
                        ) {
                            Text(
                                text = "Select Patient to talk with",
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 20.dp,
                                    bottom = 16.dp
                                ),
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(160.dp))
                            Image(
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(top = 16.dp),
                                painter = painterResource(id = R.drawable.dots),
                                contentDescription = "logo"
                            )
                        }
                    }
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
        context: Context
    ) {
        // region Content data
        val channelViewModel: ChannelViewModel = ChannelViewModel.default(context.resources)
        val channels = remember { channelViewModel.getAll() }

        CompositionLocalProvider() {
            Content(
                channels = channels,
                onSelected = {
                    println("test, doesn't work")
                    Toast.makeText(context, "this is toast message", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }


}

