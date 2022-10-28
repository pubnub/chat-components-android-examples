package com.pubnub.components.example.telehealth.viewmodel

import androidx.lifecycle.ViewModel
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.components.example.telehealth.ChatApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    lateinit var members : List<DBMemberWithChannels>

    init {
        getMembersFromDb()
    }

    private fun getMembersFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            members = ChatApplication.database.memberDao().getList()
        }
    }
}