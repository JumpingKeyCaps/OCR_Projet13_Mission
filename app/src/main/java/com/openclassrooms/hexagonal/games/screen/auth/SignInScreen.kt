package com.openclassrooms.hexagonal.games.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.theme.Purple40
import com.openclassrooms.hexagonal.games.ui.theme.Purple80

/**
 * Sign-in screen composable function.
 *
 * @param onClickGoSignUp The action to perform when the user clicks on the "Go to Sign Up" button.
 * @param onLostPassword The action to perform when the user clicks on the "Lost Password" button.
 * @param onSignIn The action to perform when the user clicks on the "Sign In" button.
 * @param signInButtonState The state of the "Sign In" button.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onClickGoSignUp: () -> Unit,
    onLostPassword: (String) -> Unit,
    onSignIn: (String,String) -> Unit,
    signInButtonState: Boolean = true){

    // Local state variables for email and password
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column( modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Spacer(modifier = Modifier.height(15.dp))

        //Icon of the app
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = " ",
            modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(15.dp))

        //Title of the screen
        Text(
            "Connectez-vous à votre compte\nHexagonal Games",
            style = MaterialTheme.typography.titleLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            fontSize = 18.sp,
            color = Purple40,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp).align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(25.dp))

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", style = MaterialTheme.typography.labelLarge) },
            placeholder = { Text("Entrez votre email", style = MaterialTheme.typography.labelLarge) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(45.dp, 0.dp, 45.dp, 0.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.labelLarge,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple80,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe", style = MaterialTheme.typography.labelLarge) },
            placeholder = { Text("Entrez votre mot de passe", style = MaterialTheme.typography.labelLarge) },
            singleLine = true,
            textStyle = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth()
                .padding(45.dp, 0.dp, 45.dp, 0.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple80,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(0.dp))

        // Password recovery button
        TextButton(modifier = Modifier.align(Alignment.CenterHorizontally),onClick = {
            onLostPassword(email)
        }) {
            Text("Mot de passe oublié ?")
        }

        Spacer(modifier = Modifier.height(40.dp))

        // sign-in button
        Button(
            onClick = { onSignIn(email, password) },
            modifier = Modifier.fillMaxWidth().padding(50.dp, 0.dp, 50.dp, 0.dp).height(50.dp).align(Alignment.CenterHorizontally),
            enabled = signInButtonState
        ) { Text("Se connecter") }

        Spacer(modifier = Modifier.height(8.dp))

        // sign-in with google button
        GoogleSignInButton(buttonText = "Se connecter avec Google",modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp).align(Alignment.CenterHorizontally), onClick = {})

        // Navigate to sign up screen button
        TextButton(modifier = Modifier
            .padding(0.dp, 15.dp, 0.dp, 0.dp)
            .align(Alignment.CenterHorizontally),
            onClick = {
                onClickGoSignUp()
            }
        ) {
            Row {
                Text("Pas encore de compte ?")
                Text(" Inscrivez-vous", color = Purple80)
            }
        }
    }
}

/**
 * Google sign-in button composable function.
 * @param buttonText The text to display on the button.
 * @param onClick The action to perform when the button is clicked.
 * @param modifier The modifier to apply to the button.
 */
@Composable
fun GoogleSignInButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, Purple80)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon (Google Logo)
            Image(
                painter = painterResource(id = R.drawable.icongoogle), // Replace with your Google icon drawable
                contentDescription = "Google Sign-In",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            // Button Text
            Text(
                text = buttonText,
                fontSize = 12.sp,
                color = Purple80
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun SignInScreenPreview(){
    SignInScreen(onClickGoSignUp = {}, onLostPassword = {}, onSignIn = { _, _ ->})
}