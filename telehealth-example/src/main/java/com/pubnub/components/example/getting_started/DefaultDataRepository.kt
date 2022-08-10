package com.pubnub.components.example.getting_started

import android.content.res.Resources
import androidx.annotation.RawRes
import com.google.gson.GsonBuilder
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.member.DBMember

internal class DefaultDataRepository(private val resources: Resources) {

    private val gson = GsonBuilder().create()

    // region Default Data
    val members: Array<DBMember> = arrayOf(
        *parseArray(R.raw.users),
    )

    val channels: Array<DBChannel> = arrayOf(
        *parseArray(R.raw.channels)
    )
    // endregion


    private inline fun <reified T> parseArray(@RawRes resource: Int): Array<out T> =
        resources.parseJson<Array<T>>(resource)

    inline fun <reified T> Resources.parseJson(@RawRes resourceId: Int): T =
        gson.fromJson(
            openRawResource(resourceId).bufferedReader().use { it.readText() },
            T::class.java
        )
}