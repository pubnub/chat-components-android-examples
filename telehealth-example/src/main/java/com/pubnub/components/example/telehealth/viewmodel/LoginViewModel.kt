package com.pubnub.components.example.telehealth.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pubnub.components.chat.provider.LocalMemberRepository
import com.pubnub.components.data.member.DBMember
import com.pubnub.components.data.member.DBMemberWithChannels
import com.pubnub.components.data.message.asMap
import com.pubnub.components.data.message.mapTo
import com.pubnub.components.example.telehealth.dto.DBMemberCustomData
import com.pubnub.components.example.telehealth.factory.LoginViewModelFactory
import com.pubnub.components.example.telehealth.repository.FakePersitentStorage
import com.pubnub.components.repository.member.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val repository: MemberRepository<DBMember, DBMemberWithChannels>,
    private val storage: FakePersitentStorage,
) : ViewModel() {

    /**
     * Log-in the user with passed `login` parameter
     *
     * @param login User `login` to log-in with
     * @return Result with [DBMemberWithChannels] or exception, if the user is not found
     */
    suspend fun login(login: String): Result<DBMemberWithChannels> =
        withContext(Dispatchers.IO) {
            val member = repository.getList().firstOrNull {
                val customData = it.custom.mapTo(DBMemberCustomData::class.java)
                login == customData?.username
            }
            if (member != null) {
                setLoggedUser(member)
                Result.success(member)
            } else {
                Result.failure(Exception())
            }
        }

    /**
     * Returns the logged user data
     *
     * @return [DBMemberWithChannels] object if exists, null otherwise
     */
    fun getLoggedUser(): DBMemberWithChannels? =
        storage.user.value

    /**
     * Stores the user data in repository
     *
     * @param user DBMemberWithChannels object
     */
    private fun setLoggedUser(user: DBMemberWithChannels?) {
        storage.user.value = user
    }

    companion object {
        @Composable
        fun default(): LoginViewModel {
            val factory = LoginViewModelFactory(
                repository = LocalMemberRepository.current,
                storage = FakePersitentStorage,
            )
            return viewModel(factory = factory)
        }
    }
}
