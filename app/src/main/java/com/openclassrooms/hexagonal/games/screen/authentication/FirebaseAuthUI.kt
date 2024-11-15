package com.openclassrooms.hexagonal.games.screen.authentication

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.hexagonal.games.R


@Composable
fun FirebaseAuthUI(onAuthSuccess: (String?) -> Unit, onAuthFailure: (Exception?) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            onAuthSuccess(user?.email)
        } else {
            val error = result.data?.getSerializableExtra("error") as? Exception
            onAuthFailure(error)
        }
    }

    val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),  // Connexion par email
        AuthUI.IdpConfig.GoogleBuilder().build() // Connexion avec Google
    )

    val intent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setTheme(R.style.Theme_HexagonalGames) // Optionnel : personnalise le thème
        .build()

    launcher.launch(intent)
}