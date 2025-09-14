package com.example.mediline.Admin.ui.Profile


import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.data.model.AdminProfile
import com.example.mediline.dl.AdminSignOutUseCase
import com.example.mediline.dl.LoadAdminProfileUseCase
import com.example.mediline.dl.UpdateAdminProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    private val loadProfileUseCase: LoadAdminProfileUseCase,
    private val updateProfileUseCase: UpdateAdminProfileUseCase,
    private val onLogout: AdminSignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminProfileUiState())
    val uiState: StateFlow<AdminProfileUiState> = _uiState

   // init { loadProfile() }

//    fun loadProfile() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, error = null) }
//            try {
//                val profile = loadProfileUseCase()
//                _uiState.update {
//                    it.copy(
//                       // name = profile.name,
//                        email = profile.email,
//                        role = profile.role,
//                       // profileImageUri = null, // preview only
//                        isLoading = false
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update { it.copy(isLoading = false, error = e.message) }
//            }
//        }
//    }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }
            try {
                val state = _uiState.value
                val profile = AdminProfile(
                    //name = state.name,
                    email = state.email,
                    role = state.role,
                   // imageUrl = "https://example.com/default_profile.jpg" // hardcoded
                )
                updateProfileUseCase(profile)
                _uiState.update { it.copy(isLoading = false, successMessage = "Profile updated successfully") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun updateName(name: String) = _uiState.update { it.copy(name = name) }
    fun updateEmail(email: String) = _uiState.update { it.copy(email = email) }
    fun updateRole(role: String) = _uiState.update { it.copy(role = role) }

    // <-- Add this function for UI preview
    fun updateProfileImage(uri: Uri) {
        _uiState.update { it.copy(profileImageUri = uri) }
    }

    fun logout(){
        onLogout()
    }

}
