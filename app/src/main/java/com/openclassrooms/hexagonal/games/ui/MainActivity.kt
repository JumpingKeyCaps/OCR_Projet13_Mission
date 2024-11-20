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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.hexagonal.games.screen.Screen
import com.openclassrooms.hexagonal.games.screen.ad.AddScreen
import com.openclassrooms.hexagonal.games.screen.auth.AuthenticationScreen
import com.openclassrooms.hexagonal.games.screen.account.MyAccountScreen
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

  /**
   * Life cycle method called when the activity is first created.
   *
   *  -  Sets up the navigation controller
   *  -  Set the Composable UI content
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
      //init FireBaseApp
      FirebaseApp.initializeApp(this)
    setContent {
      val navController = rememberNavController()
      HexagonalGamesTheme {
        HexagonalGamesNavHost(
          navHostController = navController,
          // Check if the user is logged in or not to set the start destination
          startDestination = if (FirebaseAuth.getInstance().currentUser == null) Screen.Authentication.route else Screen.Homefeed.route
        )
      }
    }
  }
}

  /**
   * Composition function for the navigation host of the app.
   * @param navHostController The navigation controller for the app.
   * @param startDestination The route of the screen to display first.
   */
@Composable
fun HexagonalGamesNavHost(navHostController: NavHostController,startDestination: String) {
    NavHost(
      navController = navHostController,
      startDestination = startDestination
    ) {
    //-- HomeFeed Screen
      composable(route = Screen.Homefeed.route) {
        HomefeedScreen(
          onPostClick = {
            //TODO
          },
          onSettingsClick = {
            navHostController.navigate(Screen.Settings.route)
          },
          onMyAccountClick = {
            navHostController.navigate(Screen.MyAccount.route)
          }, onFABClick = {
            //todo -- simple test add entry in DB


                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    Log.e("DBs", "No user is authenticated!")
                } else {
                    Log.d("DBs", "Authenticated user: ${currentUser.uid}")


                   // val db = Firebase.firestore
                    val db = FirebaseFirestore.getInstance("hexagonaldb")
                    Log.d("DBs", "TRY UPDATE DB ...")
                    db.collection("users")
                        .document(currentUser.uid)
                        .set(mapOf("firstname" to "tester","lastname" to "family"))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("DBs", "User successfully added to Firestore.")
                            } else {
                                Log.e("DBs","Error task adding user to Firestore: ${task.exception}",task.exception)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("DBs", "Error adding user to Firestore: $exception", exception)
                        }


                }

            //todo to uncomment after test ---------------------
//            navHostController.navigate(Screen.AddPost.route)
            }
        )




      }
      //-- Add Post Screen
      composable(route = Screen.AddPost.route) {
        AddScreen(
          onBackClick = { navHostController.navigateUp() },
          onSaveClick = { navHostController.navigateUp() }
        )
      }
      //-- Settings Screen
      composable(route = Screen.Settings.route) {
        SettingsScreen(
          onBackClick = { navHostController.navigateUp() }
        )
      }
      //-- Authentication Screen
      composable(route = Screen.Authentication.route) {
        AuthenticationScreen(onNavigateToHomeScreen = { navHostController.navigate(Screen.Homefeed.route) })
      }
      //-- My Account Screen
      composable(route = Screen.MyAccount.route) {
        MyAccountScreen(
          onBackClick = { navHostController.navigateUp() },
          onLogOutAction = {
            navHostController.navigate(Screen.Authentication.route) {
              //clean the nav backstack
              popUpTo(0)
              launchSingleTop = true
              restoreState = false
            }
          },
          user = FirebaseAuth.getInstance().currentUser
        )
      }
    }
}
