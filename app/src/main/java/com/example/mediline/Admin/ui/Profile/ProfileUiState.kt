package com.example.mediline.Admin.ui.Profile

import android.net.Uri


data class AdminProfileUiState(
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val profileImageUri: Uri? = null, // ✅ Profile pic
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)
