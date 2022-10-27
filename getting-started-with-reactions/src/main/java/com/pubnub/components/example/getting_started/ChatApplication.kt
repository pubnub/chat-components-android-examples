package com.pubnub.components.example.getting_started

import android.app.Application
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.data.Database
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatApplication : Application() {

    companion object {
        lateinit var database: DefaultDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Database.initialize(applicationContext) { it.prepopulate() }
    }

    private fun RoomDatabase.Builder<DefaultDatabase>.prepopulate(): RoomDatabase.Builder<DefaultDatabase> =
        addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    prepopulateData()
                }
            }
        )

    @OptIn(DelicateCoroutinesApi::class)
    private fun prepopulateData() {
        Log.e("TAG", "Database onCreate")
        // insert the data on the IO Thread
        GlobalScope.launch(Dispatchers.IO) {
            with(database) {
                val channelArray = arrayOf(Settings.channelId)

                // Creates user objects with uuid
                val members = Settings.members.map { userId ->
                    DBMember(
                        id = userId,
                        name = userId,
                        profileUrl = "https://picsum.photos/seed/${userId}/200"
                    )
                }.toTypedArray()

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
                val channels: Array<DBChannel> =
                    channelArray.map { id -> DBChannel(id, "Channel #$id") }
                        .toTypedArray()


                memberDao().insertOrUpdate(*members)
                membershipDao().insertOrUpdate(*memberships)
                channelDao().insertOrUpdate(*channels)
            }
        }
    }
}
