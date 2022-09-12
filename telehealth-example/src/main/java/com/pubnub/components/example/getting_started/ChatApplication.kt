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
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.dto.Membership
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class ChatApplication : Application() {

    companion object{
        lateinit var database: DefaultDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Database.initialize(applicationContext) { it.prepopulate(applicationContext) }
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
                        with(database) {

                            // add members
                            val members = defaultDataRepository.members
                            Log.e("DATABASE", "Add Members $members")
                            memberDao().insertOrUpdate(*members)
                            val channels = defaultDataRepository.channels
                            Log.e("DATABASE", "Add Channels $channels")
                            println("Add members $members")
                            println("Add Channels $channels")
                            channelDao().insertOrUpdate(*channels)
                            val jsonFileString =
                                getJsonDataFromAsset(applicationContext, "memberships.json")
                            val gson = Gson()
                            var dbMembership = mutableListOf<DBMembership>()
                            val listPersonType = object : TypeToken<List<Membership>>() {}.type
                            var memberships: List<Membership> =
                                gson.fromJson(jsonFileString, listPersonType)
                            memberships.forEach { membership ->
                                membership.members?.forEach {
                                    dbMembership.add(
                                        DBMembership(
                                            channelId = membership.channelId!!,
                                            memberId = it!!
                                        )
                                    )
                                }
                            }
                            println("Add memberships $dbMembership")
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