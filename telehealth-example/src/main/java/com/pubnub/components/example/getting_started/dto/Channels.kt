package com.pubnub.components.example.getting_started.dto

import com.google.gson.annotations.SerializedName

data class Channels(

    @field:SerializedName("Channels")
    val channels: List<ChannelsItem?>? = null
)

data class CustomChannel(

    @field:SerializedName("thumb")
    val thumb: String? = null
)

data class ChannelsItem(

    @field:SerializedName("custom")
    val custom: CustomChannel? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("eTag")
    val eTag: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("updated")
    val updated: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
