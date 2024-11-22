package com.openclassrooms.hexagonal.games.screen.ad

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.PostStoreRepository
import com.openclassrooms.hexagonal.games.data.repository.StorageRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This ViewModel manages data and interactions related to adding new posts in the AddScreen.
 * It utilizes dependency injection to retrieve a PostRepository instance for interacting with post data.
 */
@HiltViewModel
class AddViewModel @Inject constructor(
  private val postStoreRepository: PostStoreRepository,
  private val storageRepository: StorageRepository,
  private val userRepository: UserStoreRepository
) : ViewModel() {

  private val _currentUser = MutableStateFlow<User?>(null)
  val currentUser: StateFlow<User?> get() = _currentUser

  private var uploadResult = mutableStateOf<String?>(null)
  private var uploadError = mutableStateOf<String?>(null)



  /**
   * Internal mutable state flow representing the current post being edited.
   */
  private var _post = MutableStateFlow(Post())
  
  /**
   * Public state flow representing the current post being edited.
   * This is immutable for consumers.
   */
  val post: StateFlow<Post>
    get() = _post
  
  /**
   * StateFlow derived from the post that emits a FormError if the title is empty, null otherwise.
   */
  val error = post.map {
    verifyPost()
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = null,
  )
  
  /**
   * Handles form events like title and description changes.
   *
   * @param formEvent The form event to be processed.
   */
  fun onAction(formEvent: FormEvent) {
    when (formEvent) {
      is FormEvent.DescriptionChanged -> {
        _post.value = _post.value.copy(
          description = formEvent.description
        )
      }
      
      is FormEvent.TitleChanged -> {
        _post.value = _post.value.copy(
          title = formEvent.title
        )
      }
    }
  }

  /**
   * Add the post to the Db
   * @param userId the id of the user (post author)
   * @param imageUri the URI of the post image
   */
  fun addPost(userId: String,imageUri: Uri? = null) {

    viewModelScope.launch {
      // 1 - get the user profile from db
      userRepository.getUser(
        userId,
        onSuccess = {
            user: User ->
          _currentUser.value = user
          // 2 - upload picture (if exist) and get the url from storage
          if (imageUri != null) {
            storageRepository.uploadImage(
              imageUri = imageUri,
              onSuccess = { resultUrl ->
                uploadResult.value = resultUrl

                // 3 - add the post to the db
                postStoreRepository.addPost(_post.value.copy(author = user,photoUrl = resultUrl,timestamp = System.currentTimeMillis()),
                  onSuccess = { Log.d("addPost", "Post added successfully in DB !") },
                  onFailure = { exception -> Log.d("addPost", "Error adding post in DB : $exception") })
              },
              onError = { exception ->
                uploadError.value = exception.message
              }
            )

          }else{ // 2 - (case : no picture with the post)
            //3 - add the post to the db
            postStoreRepository.addPost(_post.value.copy(author = user,timestamp = System.currentTimeMillis()),
              onSuccess = { Log.d("addPost", "Post added successfully in DB !") },
              onFailure = { exception -> Log.d("addPost", "Error adding post in DB : $exception") })

          }

        },
        onFailure = {_currentUser.value = null})
    }

  }
  
  /**
   * Verifies mandatory fields of the post
   * and returns a corresponding FormError if so.
   *
   * @return A FormError.TitleError if title is empty, null otherwise.
   */
  private fun verifyPost(): FormError? {
    return if (_post.value.title.isEmpty()) {
      FormError.TitleError
    } else {
      null
    }
  }

}
