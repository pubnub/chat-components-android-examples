package com.pubnub.components.example.telehealth.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Parameters(val userId: String, val type: String, val channelId: String, val secondUserId: String, val secondUserName: String) : Parcelable {
    companion object {
        const val PARAMETERS_BUNDLE_KEY = "PARAMETERS_BUNDLE_KEY"
    }
}