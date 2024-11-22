package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.data.service.firestore.FirestoreService
import com.openclassrooms.hexagonal.games.domain.model.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This repository for accessing and managing user Comment data.
 */
class CommentStoreRepository @Inject constructor(private val firestoreService: FirestoreService) {

    /**
     * Add a comment to a post.
     * @param postId The ID of the post to which the comment will be added.
     * @param comment The comment to be added.
     * @param onSuccess Callback to be invoked upon successful addition of the comment.
     * @param onFailure Callback to be invoked upon failure of the addition of the comment.
     */
      fun addCommentToPost(postId: String, comment: Comment, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
          firestoreService.addCommentToPost(postId, comment, onSuccess, onFailure)
      }


    /**
     * Observe comments for a specific post.
     * @param postId The ID of the post for which comments will be observed.
     * @return A Flow emitting a list of comments for the specific post.
     */
    fun observeComments(postId: String): Flow<List<Comment>> {
        return firestoreService.observeComments(postId)
      }


}