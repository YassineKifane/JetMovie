package com.example.jetmovie.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

data class AccountUiState(
    val displayName: String = "",
    val email: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val user = auth.currentUser
        _uiState.value = _uiState.value.copy(
            displayName = user?.displayName ?: "",
            email = user?.email ?: ""
        )
    }

    fun onDisplayNameChange(value: String) {
        _uiState.value = _uiState.value.copy(displayName = value, message = null, error = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, message = null, error = null)
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(currentPassword = value, message = null, error = null)
    }

    fun onNewPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(newPassword = value, message = null, error = null)
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmNewPassword = value, message = null, error = null)
    }

    fun updateDisplayName() {
        val user = auth.currentUser ?: run {
            _uiState.value = _uiState.value.copy(error = "No authenticated user")
            return
        }

        val newName = _uiState.value.displayName
        _uiState.value = _uiState.value.copy(isLoading = true)

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        viewModelScope.launch {
            try {
                user.updateProfile(profileUpdates).await()
                _uiState.value = _uiState.value.copy(isLoading = false, message = "Profile updated")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun updateEmail() {
        val user = auth.currentUser ?: run {
            _uiState.value = _uiState.value.copy(error = "No authenticated user")
            return
        }

        val newEmail = _uiState.value.email
        val currentPassword = _uiState.value.currentPassword

        if (currentPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Current password required for sensitive changes")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val credential = EmailAuthProvider.getCredential(user.email ?: "", currentPassword)
                user.reauthenticate(credential).await()
                user.verifyBeforeUpdateEmail(newEmail).await() // ✅ método recomendado
                _uiState.value = _uiState.value.copy(isLoading = false, message = "Verification email sent to new address")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun updatePassword() {
        val user = auth.currentUser ?: run {
            _uiState.value = _uiState.value.copy(error = "No authenticated user")
            return
        }

        val currentPassword = _uiState.value.currentPassword
        val newPassword = _uiState.value.newPassword
        val confirm = _uiState.value.confirmNewPassword

        if (currentPassword.isBlank() || newPassword.isBlank() || confirm.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Fill in all password fields")
            return
        }
        if (newPassword != confirm) {
            _uiState.value = _uiState.value.copy(error = "New passwords do not match")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val credential = EmailAuthProvider.getCredential(user.email ?: "", currentPassword)
                user.reauthenticate(credential).await()
                user.updatePassword(newPassword).await()
                _uiState.value = _uiState.value.copy(isLoading = false, message = "Password updated")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            auth.signOut()
            onSignedOut()
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}
