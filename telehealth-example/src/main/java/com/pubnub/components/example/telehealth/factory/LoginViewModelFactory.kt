package com.pubnub.components.example.telehealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.components.example.telehealth.viewmodel.LoginViewModel
import com.pubnub.components.repository.member.MemberRepository

class LoginViewModelFactory constructor(
    private val repository: MemberRepository<DBMember, DBMemberWithChannels>,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                repository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}