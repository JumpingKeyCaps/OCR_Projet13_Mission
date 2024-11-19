package com.openclassrooms.hexagonal.games.screen.auth

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.hexagonal.games.ui.designcomponents.CustomShapeComponent
import com.openclassrooms.hexagonal.games.ui.theme.Purple40
import com.openclassrooms.hexagonal.games.ui.theme.Purple80
import kotlinx.coroutines.launch


/**
 * Authentication screen composable function.
 * @param viewModel The view model for the authentication screen.
 * @param onNavigateToHomeScreen The action to perform when the user is successfully authenticated.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuthenticationScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHomeScreen: () -> Unit){

    // Pager State for Horizontal Pager
    val pagerState = rememberPagerState(pageCount = { 2 })
    // Coroutine Scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()
    // Password recovery
    var showRecoveryPasswordDialog by remember { mutableStateOf(false) }
    var recoveryPasswordEmail by remember { mutableStateOf("") }
    // Infos Feedback
    val snackBarHostState = remember { SnackbarHostState() }
    //Sign-in and Sign-up button states (click locked or not)
    var signInButtonState by remember { mutableStateOf(true) }
    var signUpButtonState by remember { mutableStateOf(true) }


    //Sign-in and Sign-up results from the viewmodel
    val signInResult by viewModel.signInResult.collectAsStateWithLifecycle(null)
    val signUpResult by viewModel.signUpResult.collectAsStateWithLifecycle(null)


    // Check if the sign-in result indicates success or failure
    LaunchedEffect(signInResult) {
        signInResult?.onSuccess { user ->
            if (user != null) {
                onNavigateToHomeScreen() // Success : Navigate to the HomeScreen
            }
        }?.onFailure { exception ->
            //Failure : Show a snackbar with the error message and unlock the button to retry
            snackBarHostState.showSnackbar("Echec de la connexion: ${exception.message}")
            signInButtonState = true
        }
    }
    // Check if the sign-Up result indicates success or failure
    LaunchedEffect(signUpResult) {
        signUpResult?.onSuccess { user ->
            if (user != null) { onNavigateToHomeScreen() }
        }?.onFailure { exception ->
            snackBarHostState.showSnackbar("Echec de l'inscription: ${exception.message}")
            signUpButtonState = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.fillMaxSize()) {
            CustomShapeComponent(height = 30f, oscillations = 0, waveHeight = 0.8f)
            // Horizontal Pager for Swiping between sign-in and sign-up screens
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().padding(0.dp, 0.dp, 0.dp, 0.dp)
            ) { page ->
                when (page) {
                    // -----------  Inscription screen (Sign-Up)
                    0 -> SignUpScreen(
                        onClickGoSignIn = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1) // Navigate to page 1 (SignInScreen)
                            }
                        },
                        onSignUp = { email, password, firstName, lastName ->
                            signUpButtonState = false // lock sign up button
                            // Check if the email and password are not empty
                            if (email.isEmpty()) {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Veuillez entrer votre adresse email")
                                    signUpButtonState = true
                                }
                            }else if (password.isEmpty()){
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Veuillez entrer votre mot de passe")
                                    signUpButtonState = true
                                }
                            }else if (firstName.isEmpty()){
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Veuillez entrer votre Prenom")
                                    signUpButtonState = true
                                }
                            }else if (lastName.isEmpty()){
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Veuillez entrer votre Nom")
                                    signUpButtonState = true
                                }
                            }else{
                                //All is ok to create the new account
                                viewModel.signUpUser(email = email, password = password, firstName = firstName, lastName = lastName)
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Inscription en cours...")
                                }
                            }
                        },
                        signUpButtonState = signUpButtonState
                    )
                    // ----------- Connexion screen (Sign-In)
                    1 -> SignInScreen(
                        onClickGoSignUp = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0) // Navigate to page 0 (SignUpScreen)
                            }
                        },
                        onLostPassword = {
                            recoveryPasswordEmail = it
                            if (it.isNotEmpty()){
                                showRecoveryPasswordDialog = true // show dialog

                            }else{
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Un email est requis pour la récupération du mot de passe")
                                }
                            }
                        },
                        onSignIn = { email, password ->
                            signInButtonState = false // lock sign in button
                            if (email.isEmpty()) {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Veuillez entrer votre email")
                                    signInButtonState = true
                                }
                            }else if (password.isEmpty()){
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Veuillez entrer votre mot de passe")
                                    signInButtonState = true
                                }
                            }else{
                                //All is ok to connect
                                viewModel.signInUser(email = email, password = password)
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Connexion en cours...")
                                }
                            }
                        },
                        signInButtonState = signInButtonState
                    )
                }
            }
        }


        // Custom Dots as bottom Pager Indicator
        Row( modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 26.dp) ) {
            repeat(2) {index ->
                Surface(
                    modifier = Modifier.width(13.dp).height(13.dp).padding(2.dp),
                    color = if (pagerState.currentPage == index) Purple40 else Purple80,
                    shape = CircleShape
                ){}
            }
        }

        // Dialog for password recovery
        if (showRecoveryPasswordDialog) {
            ForgotPasswordDialog(
                onDismiss = { showRecoveryPasswordDialog = false },
                onSendEmail = {
                    // Send recovery email
                    viewModel.sendPasswordResetEmail(recoveryPasswordEmail)
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar("Un email de réinitialisation du mot de passe vous a ete envoyé!")
                    }
                    showRecoveryPasswordDialog = false
                },
                email = recoveryPasswordEmail
            )
        }

        // SnackbarHost to inform user in divers action via a snackbar
        SnackbarHost(hostState = snackBarHostState)

    }
}

/**
 * Dialog for password recovery.
 * @param onDismiss The action to perform when the dialog is dismissed.
 * @param onSendEmail The action to perform when the send email button is clicked.
 * @param email The email address to send the recovery email to.
 */
@Composable
fun ForgotPasswordDialog(onDismiss: () -> Unit, onSendEmail: () -> Unit, email:String) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Réinitialiser votre mot de passe") },
        text = {
            Text("Un email de reinitialisation du mot de passe va etre envoyé a $email.")
        },
        confirmButton = {
            Button(onClick = onSendEmail) {
                Text("Envoyer l'email")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AuthenticationScreenPreview(){
    AuthenticationScreen(onNavigateToHomeScreen = {})
}