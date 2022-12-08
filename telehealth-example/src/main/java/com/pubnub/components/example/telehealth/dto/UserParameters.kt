package com.pubnub.components.example.telehealth.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserParameters(
    val userId: String,
    val type: String,
) : Parcelable