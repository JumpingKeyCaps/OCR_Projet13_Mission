package com.openclassrooms.hexagonal.games.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.hexagonal.games.screen.Screen
import com.openclassrooms.hexagonal.games.screen.ad.AddScreen
import com.openclassrooms.hexagonal.games.screen.auth.AuthenticationScreen
import com.openclassrooms.hexagonal.games.screen.account.MyAccountScreen
import com.openclassrooms.hexagonal.games.screen.homefeed.HomefeedScreen
import com.openclassrooms.hexagonal.games.screen.postdetails.PostDetailsScreen
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
    setContent {
      val navController = rememberNavController()
      HexagonalGamesTheme {
        HexagonalGamesNavHost(
          navHostController = navController,
          // Check if the user is logged in or not to set the start destination
          startDestination = if (FirebaseAuth.getInstance().currentUser == null){
            Screen.Authentication.route
          } else{
            Screen.Homefeed.route
          }
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
          onPostClick = {post ->
            navHostController.navigate(Screen.PostDetails.createRoute(post.id))
          },
          onSettingsClick = {
            navHostController.navigate(Screen.Settings.route)
          },
          onMyAccountClick = {
            navHostController.navigate(Screen.MyAccount.route)
          }, onFABClick = {
             navHostController.navigate(Screen.AddPost.route)
            }
        )
      }
      //-- Add Post Screen
      composable(route = Screen.AddPost.route) {
        AddScreen(
          onBackClick = { navHostController.navigateUp() },
          onSaveClick = { navHostController.navigateUp() },
          userAuthId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )
      }
      //-- Settings Screen
      composable(route = Screen.Settings.route) {
        val context = LocalContext.current
        SettingsScreen(
          onBackClick = { navHostController.navigateUp() },
          onRequestNotificationAccess = { navigateToNotificationSettings(context) }
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
      //Post Details screen
      composable(
        route = Screen.PostDetails.route,
        arguments = listOf(navArgument("postId") { type = NavType.StringType })
      ) { backStackEntry ->
        PostDetailsScreen(
          postId = backStackEntry.arguments?.getString("postId") ?: "",
          onBackClick = { navHostController.navigateUp() },
          userAuthId = FirebaseAuth.getInstance().currentUser?.uid
        )
      }


    }
}

/**
 * Navigate to the notification settings screen of the phone.
 * @param context The context of the app.
 */
fun navigateToNotificationSettings(context: Context) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    // Pour Android Oreo (API 26) et supérieur, ouvre les paramètres de notifications de l'application
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
      putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
  } else {
    // Pour les versions antérieures, redirige vers les paramètres généraux (plus limité)
    val intent = Intent(Settings.ACTION_SETTINGS)
    context.startActivity(intent)
  }
}