package com.openclassrooms.hexagonal.games.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
 * Sign-up screen composition.
 * @param onClickGoSignIn The action to perform when the user clicks on the "Go to Sign In" button.
 * @param onSignUp The action to perform when the user clicks on the "Sign Up" button.
 * @param signUpButtonState The state of the "Sign Up" button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onClickGoSignIn: () -> Unit,
    onSignUp: (String,String) -> Unit,
    signUpButtonState: Boolean = true
){

    // Local state variables for email and password
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column( modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Spacer(modifier = Modifier.height(15.dp))

        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = " ",
            modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            "Rejoignez la communauté Hexagonal.",
            style = MaterialTheme.typography.titleLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            fontSize = 18.sp,
            color = Purple40,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp).align(Alignment.CenterHorizontally))
        Text(
            "Partagez vos exploits, vos astuces et échangez avec une communauté de passionnés.",
            style = MaterialTheme.typography.labelMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
            fontSize = 14.sp,
            color = Purple80,
            modifier = Modifier.padding(30.dp, 5.dp, 30.dp, 0.dp).align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(25.dp))

        // mail field
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

        // password field
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

        //Cgu infos
        Text(
            "En vous inscrivant, vous acceptez les conditions d'utilisation et la politique de confidentialité.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Light,
            fontSize = 12.sp,
            color = Purple40,
            modifier = Modifier.padding(30.dp, 20.dp, 30.dp, 0.dp).align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(30.dp))

        // Sign-Up button
        Button(
            onClick = {
                onSignUp(email, password)
            },
            modifier = Modifier.fillMaxWidth().padding(50.dp, 0.dp, 50.dp, 0.dp).height(50.dp).align(Alignment.CenterHorizontally),
            enabled = signUpButtonState
        ) {
            Text("S'inscrire")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sign in with google button
        GoogleSignInButton(buttonText = "S'inscrire avec Google",modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp).align(Alignment.CenterHorizontally), onClick = {})

        // Button to navigate to sign-in screen
        TextButton(modifier = Modifier
            .padding(0.dp, 15.dp, 0.dp, 0.dp)
            .align(Alignment.CenterHorizontally),
            onClick = {onClickGoSignIn()}
        ) {
            Row {
                Text("Tu a deja un compte ?")
                Text(" Connect toi !", color = Purple80)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){
    SignUpScreen(onClickGoSignIn = {},onSignUp = { _, _ ->})
}