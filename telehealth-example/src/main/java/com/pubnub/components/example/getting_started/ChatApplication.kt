package com.pubnub.components.example.getting_started

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.data.Database
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.dto.MembershipsItem
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Database.initialize(applicationContext){ it.prepopulate(applicationContext)}
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun RoomDatabase.Builder<DefaultDatabase>.prepopulate(context: Context): RoomDatabase.Builder<DefaultDatabase> =
        addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)


                    val defaultDataRepository = DefaultDataRepository(context.resources)

                    // insert the data on the IO Thread
                    GlobalScope.launch(Dispatchers.IO) {
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

                        }
                    }
                }
            }
        )

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