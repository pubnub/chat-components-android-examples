package com.pubnub.components.example.telehealth.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.dto.UserParameters
import com.pubnub.components.example.telehealth.extensions.parcelable

//https://stackoverflow.com/a/65619560 Currently we need to create own NavType
//Parcelable type only works in Navigation components for xml, not for Compose
class ChatParametersType : NavType<ChatParameters>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ChatParameters? {
        return bundle.parcelable(key)
    }

    override fun parseValue(value: String): ChatParameters {
        return Gson().fromJson(value, ChatParameters::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ChatParameters) {
        bundle.putParcelable(key, value)
    }
}

class UserParametersType : NavType<UserParameters>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UserParameters? {
        return bundle.parcelable(key)
    }

    override fun parseValue(value: String): UserParameters {
        return Gson().fromJson(value, UserParameters::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: UserParameters) {
        bundle.putParcelable(key, value)
    }
}