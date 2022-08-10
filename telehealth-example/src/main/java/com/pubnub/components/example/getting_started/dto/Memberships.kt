package com.pubnub.components.example.getting_started.dto

import com.google.gson.annotations.SerializedName

data class Memberships(

    @field:SerializedName("Memberships")
    val memberships: List<MembershipsItem?>? = null
)

data class Custom(
    val any: Any? = null
)

data class MembershipsItem(

    @field:SerializedName("members")
    val members: List<String?>? = null,

    @field:SerializedName("custom")
    val custom: Custom? = null,

    @field:SerializedName("channel")
    val channel: String? = null
)
