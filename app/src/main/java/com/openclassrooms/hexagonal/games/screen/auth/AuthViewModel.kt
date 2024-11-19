package com.openclassrooms.hexagonal.games.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for authentication-related operations.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository,private val userRepository: UserStoreRepository) : ViewModel() {

    private val _signUpResult = MutableSharedFlow<Result<FirebaseUser?>>()
    val signUpResult: SharedFlow<Result<FirebaseUser?>> get() = _signUpResult

    private val _signInResult = MutableSharedFlow<Result<FirebaseUser?>>()
    val signInResult: SharedFlow<Result<FirebaseUser?>> get() = _signInResult

    private val _passwordResetResult = MutableSharedFlow<Result<Unit>>()
    val passwordResetResult: SharedFlow<Result<Unit>> get() = _passwordResetResult

    /**
     * Registers a new user with the provided email and password.
     * @param email The email address of the new user.
     * @param password The password for the new user.
     */
    fun signUpUser(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            authRepository.registerUser(email, password)
                .collect { result ->

                    //on check si luser est bien ajouter
                    if (result.isSuccess) {
                        val user = result.getOrNull()
                        if (user != null) {
                            //Add user in DataBase
                            Log.d("DBUSR", "signUpUser: add user to DB ...")
                          createUserInDB(User(user.uid,firstName,lastName))
                        }


                        _signUpResult.emit(result)
                    }
                }
        }
    }


     private fun createUserInDB(user: User){
         userRepository.addUser(
             user,
             {Log.d("DBUSR", "signUpUser: OK ! user is added to DB !")},
             {Log.d("DBUSR", "signUpUser: FAIL ! user is not added to DB !")}
         )

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
                 .collect { result ->
                     _passwordResetResult.emit(result)
                 }
         }
     }


}