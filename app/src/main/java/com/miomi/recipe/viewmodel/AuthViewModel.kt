package com.miomi.recipe.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miomi.recipe.data.repository.AuthRepository
import com.miomi.recipe.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel to manage authentication state and actions
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        checkLoggedInUser()
    }

    private fun checkLoggedInUser() {
        viewModelScope.launch {
            _currentUser.value = authRepository.getLoggedInUser()
        }
    }

    fun signIn(activityContext: Context) {
        viewModelScope.launch {
            val result = authRepository.signIn(activityContext)
            if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _currentUser.value = null
        }
    }
}
