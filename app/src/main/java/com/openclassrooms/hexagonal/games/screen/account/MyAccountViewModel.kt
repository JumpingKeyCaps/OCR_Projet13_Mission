package com.openclassrooms.hexagonal.games.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing user account.
 */
@HiltViewModel
class MyAccountViewModel @Inject constructor(private val authRepository: AuthRepository,private val userStoreRepository: UserStoreRepository) : ViewModel() {

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
     * @param user The user to be deleted.
     */
    // Méthode pour supprimer un compte utilisateur
    fun deleteUserAccount(user: FirebaseUser?) {

        viewModelScope.launch {
            if (user != null) {
                //remove user in the  DataBase
                userStoreRepository.deleteUser(
                    userId = user.uid,
                    onSuccess = {
                        //remove user in the Authentication
                        removeUserAuth(user)
                    },
                    onFailure = {})
            }
        }


    }

    /**
     * Removes the user account from the Authentication.
     * @param user The user to be removed.
     */
     fun removeUserAuth(user: FirebaseUser) {
        viewModelScope.launch {
            //remove user in the Authentication
            authRepository.deleteUserAccount(user)
                .collect { result ->
                    _deleteAccountResult.emit(result)
                }
        }
    }




}