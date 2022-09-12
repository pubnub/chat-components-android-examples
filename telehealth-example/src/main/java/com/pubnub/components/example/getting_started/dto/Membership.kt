package com.pubnub.components.example.getting_started.dto

import com.google.gson.annotations.SerializedName

data class Membership(

    @field:SerializedName("members")
    val members: List<String?>? = null,

    @field:SerializedName("channelId")
    val channelId: String? = null
)
