package com.openclassrooms.hexagonal.games.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for authentication-related operations.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _signUpResult = MutableSharedFlow<Result<FirebaseUser?>>()
    val signUpResult: SharedFlow<Result<FirebaseUser?>> get() = _signUpResult

    private val _signInResult = MutableSharedFlow<Result<FirebaseUser?>>()
    val signInResult: SharedFlow<Result<FirebaseUser?>> get() = _signInResult

    /**
     * Registers a new user with the provided email and password.
     * @param email The email address of the new user.
     * @param password The password for the new user.
     */
    fun signUpUser(email: String, password: String) {
        viewModelScope.launch {
            authRepository.registerUser(email, password)
                .collect { result ->
                    _signUpResult.emit(result)
                }
        }
    }

    /**
     * Signs in a user with the provided email and password.
     * @param email The email address of the user.
     * @param password The password for the user.
     */
    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signInUser(email, password)
                .collect { result ->
                    _signInResult.emit(result)
                }
        }
    }

    /**
     * Sends a password reset email to the provided email address.
     * @param email The email address to send the reset email to.
     */
    // Méthode pour envoyer un email de réinitialisation du mot de passe
    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            authRepository.sendPasswordResetEmail(email)
        }
    }

}