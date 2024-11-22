package com.openclassrooms.hexagonal.games.screen.postdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.CommentStoreRepository
import com.openclassrooms.hexagonal.games.data.repository.PostStoreRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the screen details of a post.
 */
@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postStoreRepository: PostStoreRepository,
    private val commentStoreRepository: CommentStoreRepository,
    private val userStoreRepository: UserStoreRepository
): ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post


    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _errorComments = MutableStateFlow<String?>(null)
    val errorComments: StateFlow<String?> = _errorComments.asStateFlow()


    /**
     * Loads the post with the specified ID.
     * @param postId The ID of the post to load.
     */
    fun loadPost(postId: String) {
        viewModelScope.launch {
            postStoreRepository.getPostById(
                postId,
                onSuccess = { post ->
                    Log.d("PostDetailsScreen", "VM success : ${post?.title}")
                    _post.value = post
                            },
                onFailure = { exception -> _post.value = null}
            )
        }
    }


    /**
     * Adds a comment to a post.
     * @param postId The ID of the post to which the comment will be added.
     * @param comment The comment to add.
     */
    fun addCommentToPost(postId: String, comment: Comment) {
        viewModelScope.launch {
            // 1 -get the user profil from the db
            userStoreRepository.getUser(
                userId = comment.authorId,
                onSuccess = { user ->
                    // 2 - build and add the comment to the post.
                    commentStoreRepository.addCommentToPost(
                        postId = postId,
                        comment = comment.copy(authorName = "${user.firstname} ${user.lastname} "),
                        onSuccess = {
                            // 3 - Handle success if needed
                            Log.d("PostDetailsScreen", "addCommentToPost : success")
                        },
                        onFailure = { exception -> }
                    )
                },
                onFailure = { }
            )
        }
    }


    /**
     * Observe comments for a specific post.
     * @param postId The ID of the post for which comments will be observed.
     */
    fun observeComments(postId: String) {
        viewModelScope.launch {
            commentStoreRepository.observeComments(postId)
                .catch { exception ->
                    _errorComments.value = exception.message
                }
                .collect { comments ->
                    _comments.value = comments
                }
        }
    }

}