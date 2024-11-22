package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.data.service.firestore.FirestoreService
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This class provides a repository for accessing and managing user Post data.
 */
class PostStoreRepository @Inject constructor(private val firestoreService: FirestoreService) {

    /**
     * Adds a new Post to the data base using the injected FirestoreService.
     * @param post The Post object to be added.
     * @param onSuccess Callback to be executed on successful addition.
     * @param onFailure Callback to be executed on failure.
     */
    fun addPost(post: Post, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestoreService.addPost(post, onSuccess, onFailure)
    }


    /**
     * Get all posts and observe their changes in the data base.
     * @return A Flow emitting a list of Posts.
     */
    fun observePosts(): Flow<List<Post>> {
        return firestoreService.observePosts()
    }


    /**
     * Retrieves a specific Post by its ID from the data base.
     * @param postId The ID of the Post to retrieve.
     * @param onSuccess Callback to be executed on successful retrieval.
     * @param onFailure Callback to be executed on failure.
     */
    fun getPostById(postId: String, onSuccess: (Post?) -> Unit, onFailure: (Exception) -> Unit) {
        return firestoreService.getPostById(postId, onSuccess, onFailure)
    }



}