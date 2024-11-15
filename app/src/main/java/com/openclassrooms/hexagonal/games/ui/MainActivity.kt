package com.openclassrooms.hexagonal.games.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.hexagonal.games.screen.Screen
import com.openclassrooms.hexagonal.games.screen.ad.AddScreen
import com.openclassrooms.hexagonal.games.screen.authentication.FirebaseAuthUI
import com.openclassrooms.hexagonal.games.screen.homefeed.HomefeedScreen
import com.openclassrooms.hexagonal.games.screen.settings.SettingsScreen
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the application. This activity serves as the entry point and container for the navigation
 * fragment. It handles setting up the toolbar, navigation controller, and action bar behavior.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    setContent {
      val navController = rememberNavController()

      
      HexagonalGamesTheme {
        HexagonalGamesNavHost(
          navHostController = navController,
          startDestination = if(FirebaseAuth.getInstance().currentUser == null) Screen.Authentication.route else Screen.Homefeed.route)
      }
    }
  }
  
}

@Composable
fun HexagonalGamesNavHost(navHostController: NavHostController, startDestination: String) {
  NavHost(
    navController = navHostController,
    startDestination = startDestination
  ) {
    composable(route = Screen.Homefeed.route) {
      HomefeedScreen(
        onPostClick = {
          //TODO
        },
        onSettingsClick = {
          navHostController.navigate(Screen.Settings.route)
        },
        onFABClick = {
          navHostController.navigate(Screen.AddPost.route)
        }
      )
    }
    composable(route = Screen.AddPost.route) {
      AddScreen(
        onBackClick = { navHostController.navigateUp() },
        onSaveClick = { navHostController.navigateUp() }
      )
    }
    composable(route = Screen.Settings.route) {
      SettingsScreen(
        onBackClick = { navHostController.navigateUp() }
      )
    }

    composable(route = Screen.Authentication.route) {
      FirebaseAuthUI(
        onAuthSuccess = { email ->
          // Naviguer vers l'écran principal
          navHostController.navigate(Screen.Homefeed.route)
        },
        onAuthFailure = { error ->
          // Gérer les erreurs
          Log.d("FbAuthUI", "Authentication failed: ${error?.message}")
        }
      )
    }


  }
}
