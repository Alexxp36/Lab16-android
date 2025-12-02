package com.tecsup.eventplannerr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.eventplannerr.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle: AuthUiState()
    data class Loading(val message: String = "Cargando..."): AuthUiState()
    data class Success(val uid: String): AuthUiState()
    data class Error(val message: String): AuthUiState()
}

class AuthViewModel(private val repo: AuthRepository): ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun register(email: String, password: String) {
        _uiState.value = AuthUiState.Loading("Registrando...")
        viewModelScope.launch {
            val result = repo.register(email, password)
            if (result.isSuccess) _uiState.value = AuthUiState.Success(result.getOrThrow())
            else _uiState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Error")
        }
    }

    fun login(email: String, password: String) {
        _uiState.value = AuthUiState.Loading("Iniciando sesión...")
        viewModelScope.launch {
            val result = repo.login(email, password)
            if (result.isSuccess) _uiState.value = AuthUiState.Success(result.getOrThrow())
            else _uiState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Error")
        }
    }

    fun logout() {
        repo.logout()
        _uiState.value = AuthUiState.Idle
    }

    fun currentUid() = repo.currentUserUid()
}
