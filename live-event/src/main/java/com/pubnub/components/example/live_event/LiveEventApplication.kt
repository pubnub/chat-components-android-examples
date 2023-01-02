package com.pubnub.components.example.live_event

import android.app.Application
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.data.Database
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import jp.wasabeef.takt.Takt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LiveEventApplication : Application() {

    companion object {
        lateinit var database: DefaultDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Database.initialize(applicationContext) { it.prepopulate() }
        Takt.stock(this)
    }

    private fun RoomDatabase.Builder<DefaultDatabase>.prepopulate(): RoomDatabase.Builder<DefaultDatabase> =
        addCallback(
            object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    prepopulateMembers()
                }

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    prepopulateChannels()
                }
            }
        )

    @OptIn(DelicateCoroutinesApi::class)
    private fun prepopulateMembers() {
        Log.e("TAG", "Database prepopulate members")
        // insert the data on the IO Thread
        GlobalScope.launch(Dispatchers.IO) {
            with(database) {
                val channelArray = arrayOf(Settings.channelId)

                // Creates user objects with uuid
                val members = arrayOf(
                    DBMember(
                        id = Settings.userId,
                        name = Settings.userId,
                        profileUrl = "https://picsum.photos/seed/${Settings.userId}/200"
                    )
                )

                // Creates a membership so that the user could subscribe to channels
                val memberships: Array<DBMembership> =
                    channelArray.flatMap { channel ->
                        members.map { member ->
                            DBMembership(
                                channelId = channel,
                                memberId = member.id
                            )
                        }
                    }.toTypedArray()

                // Fills the database with member and memberships data
                memberDao().insertOrUpdate(*members)
                membershipDao().insertOrUpdate(*memberships)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun prepopulateChannels() {
        Log.e("TAG", "Database prepopulate channels")
        // insert the data on the IO Thread
        GlobalScope.launch(Dispatchers.IO) {
            with(database) {
                val channelArray = arrayOf(Settings.channelId)

                // Fills the database with channels data
                val channels: Array<DBChannel> =
                    channelArray.map { id -> DBChannel(id, "Channel #$id") }
                        .toTypedArray()

                channelDao().insertOrUpdate(*channels)
            }
        }
    }
}
