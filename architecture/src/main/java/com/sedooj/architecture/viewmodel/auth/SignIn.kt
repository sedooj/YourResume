package com.sedooj.architecture.viewmodel.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.sedooj.architecture.R
import com.sedooj.architecture.domain.repository.user.UsersNetworkRepository
import com.sedooj.architecture.viewmodel.auth.model.AuthenticationModel
import com.sedooj.architecture.viewmodel.auth.model.AuthenticationModel.*
import com.sedooj.architecture.viewmodel.auth.model.AuthorizationInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException

data class SignInUiState(
    val state: AuthState = AuthState.NOT_AUTHORIZED,
    val error: Int? = null,
    val coldStartAttempted: Boolean = false,
)

class SignInViewModel : ViewModel(), AuthenticationModel {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()
    override fun updatePageState(state: AuthState) {
        _uiState.update {
            it.copy(state = state)
        }
    }

    override fun resetErrorState() {
        _uiState.update {
            it.copy(error = null)
        }
    }
    fun resetAll() {
        _uiState.update {
            it.copy(state = AuthState.NOT_AUTHORIZED)
        }
    }

    override fun setError(msg: Int) {
        _uiState.update {
            it.copy(
                error = msg
            )
        }
    }

    private fun setColdAuthAttempted() {
        _uiState.update {
            it.copy(
                coldStartAttempted = true
            )
        }
    }

    suspend fun coldAuth(
        usersNetworkRepository: UsersNetworkRepository,
        scope: CoroutineScope,
        context: Context,
    ) {
        updatePageState(state = AuthState.AUTHORIZATION)
        scope.launch {
            try {
                doRequest(
                    usersNetworkRepository = usersNetworkRepository,
                    context = context,
                    scope = this
                )
            } catch (e: ConnectException) {
                setError(R.string.no_connection)
                updatePageState(state = AuthState.NOT_AUTHORIZED)
            }
            return@launch
        }
        setColdAuthAttempted()
    }

    override fun validateInput(input: AuthorizationInput): Boolean {
        if (input.username.isBlank()) {
            updatePageState(state = AuthState.NOT_AUTHORIZED)
            setError(R.string.wrong_username_or_password)
            return false
        }
        if (input.password.isBlank()) {
            updatePageState(state = AuthState.NOT_AUTHORIZED)
            setError(R.string.wrong_username_or_password)
            return false
        }
        if (input.username.length < 6) {
            updatePageState(state = AuthState.NOT_AUTHORIZED)
            setError(R.string.wrong_username_length)
            return false
        }
        if (input.password.length < 8) {
            updatePageState(state = AuthState.NOT_AUTHORIZED)
            setError(R.string.wrong_password_length)
            return false
        }
        return true
    }

    override fun auth(
        input: AuthorizationInput,
        usersNetworkRepository: UsersNetworkRepository,
        scope: CoroutineScope,
        context: Context,
    ) {
        updatePageState(state = AuthState.AUTHORIZATION)
        if (!validateInput(input = input)) return
        scope.launch {
            try {
                doRequest(
                    input = input,
                    usersNetworkRepository = usersNetworkRepository,
                    context = context,
                    scope = this
                )
            } catch (e: ConnectException) {
                setError(R.string.no_connection)
                updatePageState(state = AuthState.NOT_AUTHORIZED)
            }
            return@launch
        }
    }

    private suspend fun doRequest(
        input: AuthorizationInput? = null,
        usersNetworkRepository: UsersNetworkRepository,
        context: Context,
        scope: CoroutineScope,
    ) {
        val db = Room.databaseBuilder(
            context = context,
            AppDatabase::class.java, "resumen-app-db"
        ).build()
        val authUserDao = db.authUserDao()
        val authorizationData = authUserDao.getAuthorizationData()
        var userDetails: AuthorizationInput? = input
        if (userDetails == null)
            if (authorizationData != null)
                userDetails = AuthorizationInput(
                    authorizationData.username,
                    authorizationData.password
                )
            else {
                updatePageState(state = AuthState.NOT_AUTHORIZED)
                return
            }

        val response = usersNetworkRepository.auth(
            input = AuthUserInput(
                username = userDetails.username,
                password = userDetails.password
            )
        )
        when (response) {
            404 -> {
                setError(R.string.user_not_found)
                updatePageState(state = AuthState.NOT_AUTHORIZED)
            }

            403 -> {
                setError(R.string.wrong_username_or_password)
                updatePageState(state = AuthState.NOT_AUTHORIZED)
            }

            200 -> {
                scope.launch {
                    if (authorizationData == null) {
                        authUserDao.insert(
                            auth = AuthUserEntity(
                                id = 1,
                                username = userDetails.username,
                                password = userDetails.password
                            )
                        )
                    } else {
                        authUserDao.update(
                            auth = AuthUserEntity(
                                id = 1,
                                username = userDetails.username,
                                password = userDetails.password
                            )
                        )
                    }
                    return@launch
                }
                resetErrorState()
                updatePageState(state = AuthState.AUTHORIZED)
            }

            else -> {
                setError(R.string.unknown_error)
                updatePageState(state = AuthState.NOT_AUTHORIZED)
            }
        }
    }
}