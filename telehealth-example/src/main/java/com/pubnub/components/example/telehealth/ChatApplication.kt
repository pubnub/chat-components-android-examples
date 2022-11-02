package com.pubnub.components.example.telehealth

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.data.Database
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

        database = Database.initialize(applicationContext) { it.prepopulate(applicationContext) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun RoomDatabase.Builder<DefaultDatabase>.prepopulate(context: Context): RoomDatabase.Builder<DefaultDatabase> =
        addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val defaultDataRepository = DefaultDataRepository(context.resources)

                    // insert the data on the IO Thread
                    GlobalScope.launch(Dispatchers.IO) {
                        with(database) {

                            // add members
                            val members = defaultDataRepository.members
                            Log.e("DATABASE", "Add Members $members")
                            memberDao().insertOrUpdate(*members)
                            // add channels
                            val channels = defaultDataRepository.channels
                            Log.e("DATABASE", "Add Channels $channels")
                            channelDao().insertOrUpdate(*channels)
                            // add memberships
                            val memberships = defaultDataRepository.dbMemberships
                            membershipDao().insertOrUpdate(*memberships.toTypedArray())
                        }
                    }
                }
            }
        )
}