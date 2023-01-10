package com.pubnub.components.example.telehealth.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatParameters(
    val userId: String,
    val userType: String,
    val channelId: String,
    val secondUserId: String,
    val secondUserName: String,
) : Parcelable