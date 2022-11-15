package com.pubnub.components.example.telehealth.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pubnub.components.chat.provider.LocalMemberRepository
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.components.data.message.asMap
import com.pubnub.components.example.telehealth.factory.LoginViewModelFactory
import com.pubnub.components.repository.member.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val repository: MemberRepository<DBMember, DBMemberWithChannels>,
) : ViewModel() {

    suspend fun login(login: String): Result<DBMemberWithChannels> =
        withContext(Dispatchers.IO) {
            val member = repository.getList().firstOrNull {
                login == it.custom.asMap()?.get("username") as? String?
            }
            if (member != null) {
                Result.success(member)
            } else {
                Result.failure(Exception())
            }
        }

    companion object {
        @Composable
        fun default(): LoginViewModel {
            val factory = LoginViewModelFactory(
                repository = LocalMemberRepository.current,
            )
            return viewModel(factory = factory)
        }
    }
}
