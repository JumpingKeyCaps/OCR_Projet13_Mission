package com.openclassrooms.hexagonal.games.screen.homefeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.PostStoreRepository
import com.openclassrooms.hexagonal.games.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing data and events related to the Homefeed.
 * This ViewModel retrieves posts from the PostRepository and exposes them as a Flow<List<Post>>,
 * allowing UI components to observe and react to changes in the posts data.
 */
@HiltViewModel
class HomefeedViewModel @Inject constructor(private val postStoreRepository: PostStoreRepository) :
  ViewModel() {
  
  private val _posts = MutableStateFlow<List<Post>>(emptyList())
  val posts: StateFlow<List<Post>> = _posts.asStateFlow()

  private val _error = MutableStateFlow<String?>(null)
  val error: StateFlow<String?> = _error.asStateFlow()

  
  init {
    observePosts()
  }

  private fun observePosts() {
    viewModelScope.launch {
      postStoreRepository.observePosts()
        .catch { exception ->
          _error.value = exception.message
        }
        .collect { newPosts ->
          _posts.value = newPosts
        }
    }
  }

}
