package com.pubnub.components.example.telehealth.viewmodel

import androidx.lifecycle.ViewModel
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.components.data.message.asMap
import com.pubnub.components.example.telehealth.ChatApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    suspend fun login(login: String): Result<DBMemberWithChannels> =
        withContext(Dispatchers.IO) {
            val member = ChatApplication.database.memberDao().getList().firstOrNull {
                login == it.custom.asMap()?.get("username") as? String?
            }
            if (member != null) {
                Result.success(member)
            } else {
                Result.failure(Exception())
            }
        }
}
