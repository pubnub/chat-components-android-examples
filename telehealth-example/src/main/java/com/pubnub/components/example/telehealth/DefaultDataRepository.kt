package com.pubnub.components.example.telehealth

import android.content.res.Resources
import androidx.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.telehealth.dto.Membership
import com.pubnub.components.example.telehealth_example.R

/**
 *
 * This class is for providing example data from raw json files, and input it into the PubnubDatabase.
 *
 */
class DefaultDataRepository(private val resources: Resources) {

    val gson: Gson = GsonBuilder().create()

    val members: Array<DBMember> = parseArray(R.raw.users)

    val channels: Array<DBChannel> = parseArray(R.raw.channels)

    private val rawMemberships: Array<Membership> = parseArray(R.raw.memberships)

    val memberships: Array<DBMembership> = rawMemberships.flatMap { membership ->
        membership.members.map { DBMembership(membership.channelId, it) }
    }.toTypedArray()

    private inline fun <reified T> parseArray(@RawRes resource: Int): Array<T> =
        resources.parseJson(resource)

    inline fun <reified T> Resources.parseJson(@RawRes resourceId: Int): T =
        gson.fromJson(
            openRawResource(resourceId).bufferedReader().use { it.readText() },
            T::class.java
        )
}