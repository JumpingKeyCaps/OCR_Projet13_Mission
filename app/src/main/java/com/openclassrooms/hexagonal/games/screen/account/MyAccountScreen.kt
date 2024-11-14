package com.openclassrooms.hexagonal.games.screen.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.designcomponents.CustomShapeComponent
import com.openclassrooms.hexagonal.games.ui.theme.Purple40
import com.openclassrooms.hexagonal.games.ui.theme.Purple80
import kotlinx.coroutines.launch
import java.util.Date

/**
 * The user account screen composition
 * @param onBackClick The action to perform when the back button is clicked.
 * @param onLogOutAction The action to perform when the user logs out.
 * @param viewModel The view model for the user account screen.
 * @param user The current user.
 */
@Composable
fun MyAccountScreen(
    onBackClick: () -> Unit,
    onLogOutAction: () -> Unit,
    viewModel: MyAccountViewModel = hiltViewModel(),
    user: FirebaseUser?
){

    val email = user?.email ?: "Non défini"
    val uid = user?.uid ?: "Non défini"
    val creationDate = user?.metadata?.creationTimestamp?.let { Date(it).toString() } ?: "Non défini"
    val lastSignInDate = user?.metadata?.lastSignInTimestamp?.let { Date(it).toString() } ?: "Non défini"


    // Coroutine Scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()
    //Infos Feedback
    val snackBarHostState = remember { SnackbarHostState() }
    //Dialog state of log out
    var showLogOutDialog by remember { mutableStateOf(false) }

    //Result of sign out from viewmodel
    val signOutResult by viewModel.signOutResult.collectAsStateWithLifecycle(null)
    //Check if the sign out result indicates success or failure
    LaunchedEffect(signOutResult) {
        signOutResult?.onSuccess {
            // Go to authentication screen.
            onLogOutAction()
        }?.onFailure {
            // show message to user for the sign out failure
            snackBarHostState.showSnackbar("Echec de la deconnexion, veuillez réessayer.")
        }
    }


    //Dialog state of account deletion
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    //Result of delete account from viewmodel
    val deleteUserAccountResult by viewModel.deleteAccountResult.collectAsStateWithLifecycle(null)
    //check account deletion result success or failure
    LaunchedEffect(deleteUserAccountResult){
        deleteUserAccountResult?.onSuccess {
            // go to autentification screen.
            onLogOutAction()
        }?.onFailure {
            snackBarHostState.showSnackbar("Echec de la suppression du compte, veuillez réessayer.")
        }
    }





    Box(modifier = Modifier.fillMaxSize().padding(0.dp, 0.dp, 0.dp, 0.dp)){

        //TOP DECO -----------
        CustomShapeComponent(height = 65f, oscillations = 0, waveHeight = 1.0f)

        //BACK ARROW BUTTON  ----------------
        IconButton(onClick = {
            onBackClick()
        },
            modifier = Modifier.padding(16.dp, 6.dp, 0.dp, 0.dp).align(Alignment.TopStart)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = Color.White,
                contentDescription = stringResource(id = R.string.contentDescription_go_back)
            )
        }

        //CONTENTS   ---------------------
        Column(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(0.dp, 90.dp, 0.dp, 0.dp)) {
            Text(stringResource(id = R.string.myAccount_fragment_label),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraLight,
                color = Purple40,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(20.dp, 0.dp, 20.dp, 0.dp))

            Spacer(modifier = Modifier.height(22.dp))

            Text(text = "Retrouvez ici l'ensemble de vos informations\n lier a votre compte utilisateur.",
                style = MaterialTheme.typography.bodyMedium,
                color = Purple80,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp).align(Alignment.CenterHorizontally))


            Spacer(modifier = Modifier.height(36.dp))

            // Informations de l'utilisateur
            UserInfoCard(label = "Email", value = email)
            UserInfoCard(label = "ID Compte", value = uid)
            UserInfoCard(label = "Date de création", value = creationDate)
            UserInfoCard(label = "Dernière connexion", value = lastSignInDate)

        }


        //BOTTOM BUTTONS ----------
        Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 30.dp)){
            // Sign out button
            Button(
                onClick = {
                    //show confirme dialog
                    showLogOutDialog = true
                },
                modifier = Modifier.fillMaxWidth().padding(36.dp, 0.dp, 36.dp, 0.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple40)
            ) {
                Text(text = "Se déconnecter")
            }

            // Delete account button
            Button(
                onClick = {
                    showDeleteAccountDialog = true
                },
                modifier = Modifier.fillMaxWidth().padding(46.dp, 15.dp, 46.dp, 0.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Supprimer mon compte", color = Color.White)
            }
        }


    }


    // Dialog of Log out validation
    if (showLogOutDialog) {
        LogActionDialog(
            onDismiss = { showLogOutDialog = false },
            onValidate = {
                //logout
                viewModel.signOutUser()
                showLogOutDialog = false
                coroutineScope.launch {
                    snackBarHostState.showSnackbar("Deconnexion en cours...")
                }
            },
            typeOfDialog = 1
        )
    }

    // Dialog of delete account validation
    if (showDeleteAccountDialog) {
        LogActionDialog(
            onDismiss = { showDeleteAccountDialog = false },
            onValidate = {
                //logout
                viewModel.deleteUserAccount()
                showDeleteAccountDialog = false
                coroutineScope.launch {
                    snackBarHostState.showSnackbar("Suppression de votre compte en cours...")
                }
            },
            typeOfDialog = 2
        )
    }

    // Snackbar host to inform user
    SnackbarHost(hostState = snackBarHostState)

}


/**
 * A composable function to display a user information card.
 * @param label The label for the user information.
 * @param value The value for the user information.
 */
@Composable
fun UserInfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 24.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

/**
 * A composable function to display a log-out or account deletion confirmation dialog.
 * @param onDismiss The action to perform when the dialog is dismissed.
 * @param onValidate The action to perform when the validation button is clicked.
 * @param typeOfDialog The type of dialog to display (1 for log out, 2 for account deletion).
 */
@Composable
fun LogActionDialog(onDismiss: () -> Unit, onValidate: () -> Unit, typeOfDialog: Int) {

    val title = when(typeOfDialog){
        1 -> "Se déconnecter"
        2 -> "Supprimer mon compte"
        else -> ""
    }

    val message = when(typeOfDialog){
        1 -> "Êtes-vous sûr de vouloir vous déconnecter?"
        2 -> "Êtes-vous sûr de vouloir supprimer votre compte?\nAttention! Cette action est irréversible."
        else -> ""
    }

    val buttonText = when(typeOfDialog){
        1 -> "Se déconnecter"
        2 -> "Supprimer"
        else -> ""
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(onClick = onValidate) {
                Text(text = buttonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Annuler")
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun MyAccountScreenPreview(){
    MyAccountScreen(onBackClick = {}, onLogOutAction = {},user = null)
}