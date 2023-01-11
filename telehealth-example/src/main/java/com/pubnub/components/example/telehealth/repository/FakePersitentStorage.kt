package com.pubnub.components.example.telehealth.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.framework.data.UserId

/**
 * This object is created to store some runtime data.
 *
 * In our example, the user have to login each time the application is launched. In real use-case
 * it should be stored in database (in custom fields) or shared preferences.
 */
object FakePersitentStorage {
    var user: MutableState<DBMemberWithChannels?> = mutableStateOf(null)
}