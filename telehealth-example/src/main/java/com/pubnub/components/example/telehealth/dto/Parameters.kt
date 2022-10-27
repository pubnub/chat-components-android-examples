package com.pubnub.components.example.telehealth.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Parameters(val userId: String, val type: String) : Parcelable {
    companion object {
        const val PARAMETERS_BUNDLE_KEY = "PARAMETERS_BUNDLE_KEY"
    }
}