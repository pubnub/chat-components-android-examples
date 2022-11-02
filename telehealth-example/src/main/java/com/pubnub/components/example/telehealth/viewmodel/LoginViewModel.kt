package com.pubnub.components.example.telehealth.viewmodel

import androidx.lifecycle.ViewModel
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.components.data.message.asMap
import com.pubnub.components.example.telehealth.ChatApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    suspend fun verifyMember(login: String): DBMemberWithChannels? {
        CoroutineScope(Dispatchers.IO).launch {
            val member = ChatApplication.database.memberDao().getList().firstOrNull {
                login == it.custom.asMap()?.get("username") as? String?
            }

            return@launch member
        }
    }
}