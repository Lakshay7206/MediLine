package com.example.mediline.Admin.ui.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.dl.LoginAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminLoginViewModel @Inject constructor(
 private val loginAdminUseCase: LoginAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminLoginState())
    val uiState: StateFlow<AdminLoginState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {

        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = uiState.value.copy(password = password)
    }

    fun loginAdmin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result=loginAdminUseCase(_uiState.value.email, _uiState.value.password)
            result.fold(
                onSuccess = {
                    onSuccess()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(error = e.message ?: "Login failed")
                }
            )
        }
    }
}

data class AdminLoginState(
    val email: String = "",
    val password: String = "",
    val error: String = ""
)
