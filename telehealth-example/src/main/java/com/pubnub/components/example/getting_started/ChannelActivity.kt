package com.pubnub.components.example.getting_started

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.asPubNub
import com.pubnub.components.chat.provider.ChatProvider
import com.pubnub.components.chat.provider.LocalChannelRepository
import com.pubnub.components.chat.provider.LocalMemberRepository
import com.pubnub.components.chat.provider.LocalMembershipRepository
import com.pubnub.components.chat.ui.component.channel.ChannelList
import com.pubnub.components.chat.ui.component.channel.ChannelUi
import com.pubnub.components.chat.viewmodel.channel.ChannelViewModel
import com.pubnub.components.data.Database
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.dto.MembershipsItem
import com.pubnub.components.example.getting_started.ui.theme.AppTheme
import com.pubnub.components.example.getting_started.ui.view.Channel
import com.pubnub.components.example.getting_started.ui.view.Chat
import com.pubnub.framework.data.ChannelId
import kotlinx.coroutines.*
import java.io.IOException

class ChannelActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePubNub()
        setContent {
            channelsWithLocalDb()

            }
    }

    @DelicateCoroutinesApi
    @Composable
    private fun channelsWithLocalDb(){
        val db = Database.initialize(applicationContext){ it.prepopulate(applicationContext)}
        //val db = Room.databaseBuilder(this, DefaultDatabase::class.java, "database-name").prepopulate(this).build()
        AppTheme(pubNub = pubNub, database = db.asPubNub(),) {
            Box(modifier = Modifier.fillMaxSize()) {
           Channel.View(context = applicationContext)
            }
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
        val channelRepository = LocalChannelRepository.current
        val channels: Array<DBChannel> =
            channelId.map { id -> DBChannel(id, "") }.toTypedArray()

        val scope = rememberCoroutineScope()
        LaunchedEffect(null) {
            scope.launch {
                memberRepository.insertOrUpdate(member)
                membershipRepository.insertOrUpdate(*memberships)
                channelRepository.insertOrUpdate(*channels)
            }
        }
    }

    private fun initializeChannels(context: Context) {
        val jsonFileString = getJsonDataFromAsset(context, "channels.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<DBChannel>>() {}.type
        var channels: List<DBChannel> = gson.fromJson(jsonFileString, listPersonType)
        channels.forEach {
            println(it.name)
        }
    }

    @DelicateCoroutinesApi
    fun RoomDatabase.Builder<DefaultDatabase>.prepopulate(context: Context): RoomDatabase.Builder<DefaultDatabase> =
        addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val defaultDataRepository = DefaultDataRepository(context.resources)

                    // insert the data on the IO Thread
                    GlobalScope.launch(Dispatchers.Main.immediate) {
                        with(Database.INSTANCE) {

                            // add members
                            val members = defaultDataRepository.members
                            Log.e("DATABASE", "Add Members $members")
                            memberDao().insertOrUpdate(*members)
                            val channels = defaultDataRepository.channels
                            Log.e("DATABASE", "Add Channels $channels")
                            println("Add members $members")
                            println("Add Channels $channels")
                            channelDao().insertOrUpdate(*channels)
                            val jsonFileString = getJsonDataFromAsset(applicationContext, "membership.json")
                            val gson = Gson()
                            var dbMembership = mutableListOf<DBMembership>()
                            val listPersonType = object : TypeToken<List<MembershipsItem>>() {}.type
                            var memberships: List<MembershipsItem> = gson.fromJson(jsonFileString, listPersonType)
                            memberships.forEach { membership ->
                                membership.members?.forEach {
                                    dbMembership.add(DBMembership(channelId = membership.channel!!,memberId = it!!))
                                }
                            }
                            membershipDao().insertOrUpdate(*dbMembership.toTypedArray())

//                        val messages = defaultDataRepository.messages
//                        messageDao().insert(*messages)
                        }
                    }
                }
            }
        )



    private fun initializePubNub() {
        pubNub = PubNub(
            PNConfiguration(uuid = "myFirstUser").apply {
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.NONE
            }
        )
    }


    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}