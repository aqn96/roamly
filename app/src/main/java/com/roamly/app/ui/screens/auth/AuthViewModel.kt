package com.roamly.app.ui.screens.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roamly.app.data.AuthRepository
import com.roamly.app.data.RoamlyUser
import com.roamly.app.data.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * What: The "brain" for the auth flow (Login, Sign Up, Create Profile). It validates input,
 *       calls AuthRepository against Firebase, and exposes a single sealed AuthUiState via
 *       StateFlow so screens can show Loading / errors and react to Success by navigating
 *       (course Topic 06 ViewModel + StateFlow + sealed-state pattern).
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()
    private val storage = StorageRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) = viewModelScope.launch {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Please enter your email and password")
            return@launch
        }
        _uiState.value = AuthUiState.Loading
        repo.login(email.trim(), password)
            .onSuccess { _uiState.value = AuthUiState.Success }
            .onFailure { _uiState.value = AuthUiState.Error(it.message ?: "Login failed") }
    }

    fun signUp(fullName: String, email: String, password: String, confirmPassword: String) = viewModelScope.launch {
        when {
            fullName.isBlank() -> _uiState.value = AuthUiState.Error("Please enter your name")
            email.isBlank() -> _uiState.value = AuthUiState.Error("Please enter your email")
            password.length < 6 -> _uiState.value = AuthUiState.Error("Password must be at least 6 characters")
            password != confirmPassword -> _uiState.value = AuthUiState.Error("Passwords don't match")
            else -> {
                _uiState.value = AuthUiState.Loading
                repo.signUp(fullName.trim(), email.trim(), password)
                    .onSuccess { _uiState.value = AuthUiState.Success }
                    .onFailure { _uiState.value = AuthUiState.Error(it.message ?: "Sign-up failed") }
            }
        }
    }

    fun saveProfile(
        username: String,
        homeCountry: String,
        favoriteDestination: String,
        travelStyle: String,
        travelFrequency: String,
        avatarUri: Uri? = null,
    ) = viewModelScope.launch {
        if (username.isBlank()) {
            _uiState.value = AuthUiState.Error("Please choose a username")
            return@launch
        }
        _uiState.value = AuthUiState.Loading
        val avatarUrl = if (avatarUri != null) {
            val result = storage.uploadProfilePhoto(avatarUri)
            if (result.isFailure) {
                _uiState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Could not upload profile photo")
                return@launch
            }
            result.getOrThrow()
        } else {
            ""
        }
        val profile = RoamlyUser(
            username = username.trim(),
            homeCountry = homeCountry.trim(),
            favoriteDestination = favoriteDestination.trim(),
            travelStyle = travelStyle,
            travelFrequency = travelFrequency,
            avatarUrl = avatarUrl,
        )
        repo.saveProfile(profile)
            .onSuccess { _uiState.value = AuthUiState.Success }
            .onFailure { _uiState.value = AuthUiState.Error(it.message ?: "Could not save profile") }
    }

    /** Resets back to Idle after a screen has consumed a Success/Error. */
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
