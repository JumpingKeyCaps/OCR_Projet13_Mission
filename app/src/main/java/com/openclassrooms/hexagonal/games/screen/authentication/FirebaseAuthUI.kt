package com.openclassrooms.hexagonal.games.screen.authentication

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.hexagonal.games.R


@Composable
fun FirebaseAuthUI(onAuthSuccess: (String?) -> Unit, onAuthFailure: (Exception?) -> Unit) {

    // État pour contrôler le lancement unique
    var hasLaunched by remember { mutableStateOf(false) }

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
        .setIsSmartLockEnabled(false, true)
        .setTosAndPrivacyPolicyUrls("https://example.com/terms", "https://example.com/privacy")
        .setLogo(R.mipmap.ic_launcher_foreground)
        .setTheme(R.style.Theme_HexagonalGames) // Optionnel : personnalise le thème
        .build()

    // Lancer l'intent uniquement une fois
    LaunchedEffect(Unit) {
        if (!hasLaunched) {
            launcher.launch(intent)
            hasLaunched = true // Marque comme lancé
        }
    }


}