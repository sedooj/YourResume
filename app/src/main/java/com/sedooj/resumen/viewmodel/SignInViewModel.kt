package com.sedooj.resumen.viewmodel

import androidx.lifecycle.ViewModel
import com.sedooj.resumen.R
import com.sedooj.resumen.domain.data.user.create.CreateUserInput
import com.sedooj.resumen.domain.data.user.create.auth.AuthUserInput
import com.sedooj.resumen.domain.repository.user.UsersNetworkRepository
import com.sedooj.resumen.viewmodel.models.AuthenticationModel
import com.sedooj.resumen.viewmodel.models.AuthenticationModel.*
import com.sedooj.resumen.viewmodel.models.AuthorizationInput
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

    override fun setError(msg: Int) {
        _uiState.update {
            it.copy(
                error = msg
            )
        }
    }

    override fun auth(
        input: AuthorizationInput,
        usersNetworkRepository: UsersNetworkRepository,
        scope: CoroutineScope,
    ) {
        updatePageState(state = AuthState.AUTHORIZATION)
        if (!validateInput(
                input = input
            )
        ) return
        scope.launch {
            try {
                val response = usersNetworkRepository.auth(
                    input = AuthUserInput(
                        username = input.username,
                        password = input.password
                    )
                )
                when(response) {
                    404 -> {
                        setError(R.string.user_not_found)
                        updatePageState(state = AuthState.NOT_AUTHORIZED)
                    }
                    403 -> {
                        setError(R.string.wrong_username_or_password)
                        updatePageState(state = AuthState.NOT_AUTHORIZED)
                    }
                    200 -> {
                        resetErrorState()
                        updatePageState(state = AuthState.AUTHORIZED)
                    }
                    else -> {
                        setError(R.string.unknown_error)
                        updatePageState(state = AuthState.NOT_AUTHORIZED)
                    }
                }
            } catch (e: ConnectException) {
                setError(R.string.no_connection)
                updatePageState(state = AuthState.NOT_AUTHORIZED)
                e.printStackTrace()
            }
            return@launch
        }
    }
}