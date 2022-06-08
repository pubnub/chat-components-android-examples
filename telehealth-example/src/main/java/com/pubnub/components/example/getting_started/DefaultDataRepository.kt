package com.pubnub.components.example.getting_started

import android.content.res.Resources
import androidx.annotation.RawRes
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.pubnub.components.chat.ui.mapper.channel.DBChannelMapper
import com.pubnub.components.data.channel.DBChannel
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.data.message.DBAttachment
import com.pubnub.components.data.message.DBMessage
import java.lang.reflect.Type

internal class DefaultDataRepository(private val resources: Resources) {

    private val gson = GsonBuilder().registerTypeAdapter(
        DBAttachment::class.java,
        attachmentDeserializer()
    ).create()

    // region Default Data
    val members: Array<DBMember> = arrayOf(
        *parseArray(R.raw.doctors),
        *parseArray(R.raw.patients),
    )

    val channels: Array<DBChannel> = arrayOf(
        *parseArray(R.raw.channels)
    )
    // endregion

    fun attachmentDeserializer(): JsonDeserializer<DBAttachment> =
        JsonDeserializer { jsonElement: JsonElement, _: Type, _: JsonDeserializationContext ->
            val jsonObject = jsonElement.asJsonObject

            when (val objectType = jsonObject.get("type").asString) {
                "link" -> gson.fromJson(jsonElement.toString(), DBAttachment.Link::class.java)
                "image" -> gson.fromJson(jsonElement.toString(), DBAttachment.Image::class.java)
                else -> throw RuntimeException("Unknown type '$objectType'")
            }

        }

    private inline fun <reified T> parseArray(@RawRes resource: Int): Array<out T> =
        resources.parseJson<Array<T>>(resource)

    inline fun <reified T> Resources.parseJson(@RawRes resourceId: Int): T =
        gson.fromJson(
            openRawResource(resourceId).bufferedReader().use { it.readText() },
            T::class.java
        )
}