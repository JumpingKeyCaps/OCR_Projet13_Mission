package com.openclassrooms.hexagonal.games.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing user account.
 */
@HiltViewModel
class MyAccountViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _signOutResult = MutableSharedFlow<Result<Unit>>()
    val signOutResult: SharedFlow<Result<Unit>> get() = _signOutResult

    private val _deleteAccountResult = MutableSharedFlow<Result<Unit>>()
    val deleteAccountResult: SharedFlow<Result<Unit>> get() = _deleteAccountResult


    /**
     * Signs out the current user.
     */
    // Méthode pour déconnecter un utilisateur
    fun signOutUser() {
        viewModelScope.launch {
            authRepository.signOutUser()
                .collect { result ->
                    _signOutResult.emit(result)
                }
        }
    }

    /**
     * Deletes the current user's account.
     */
    // Méthode pour supprimer un compte utilisateur
    fun deleteUserAccount() {
        viewModelScope.launch {
            authRepository.deleteUserAccount()
                .collect { result ->
                    _deleteAccountResult.emit(result)
                }
        }
    }
}