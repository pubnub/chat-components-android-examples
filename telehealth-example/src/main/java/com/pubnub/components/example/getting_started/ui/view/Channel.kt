package com.pubnub.components.example.getting_started.ui.view


import android.content.Context
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.paging.PagingData
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.chat.ui.component.channel.ChannelList
import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.ui.component.provider.LocalChannel
import com.pubnub.components.chat.viewmodel.channel.ChannelViewModel
import com.pubnub.components.data.Database
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.DefaultDataRepository
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

    @Composable
    internal fun Content(
        channels: Flow<PagingData<ChannelUi>>,
        onSelected: (ChannelUi.Data) -> Unit = {},
    ) {
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
            ChannelList(
                channels = channels,
                onSelected = onSelected
            )
        }
    }

    @Composable
    fun View(
        context: Context
    ) {
        // region Content data
        val channelViewModel: ChannelViewModel = ChannelViewModel.default(context.resources)
        val channels = remember { channelViewModel.getAll() }



        val state by channels.collectAsState(initial = emptyList<ChannelUi>())
        println(state.toString())

        CompositionLocalProvider() {
            Content(
                channels = channels,
                onSelected = {

                }
            )
        }
    }


}

