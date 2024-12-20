package com.openclassrooms.hexagonal.games.screen

import androidx.navigation.NamedNavArgument

sealed class Screen(
  val route: String,
  val navArguments: List<NamedNavArgument> = emptyList()
) {
  data object Homefeed : Screen("homefeed")
  
  data object AddPost : Screen("addPost")
  
  data object Settings : Screen("settings")

  data object Authentication : Screen("authentication")

  data object MyAccount : Screen("myAccount")

  data object PostDetails : Screen("postDetails/{postId}") {
    fun createRoute(postId: String): String = "postDetails/$postId"
  }
}