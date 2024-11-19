package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.data.service.firestore.FirestoreService
import com.openclassrooms.hexagonal.games.domain.model.Post
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
     * Retrieves a list of all Posts from the data base using the injected FirestoreService.
     * @param onSuccess Callback to be executed on successful retrieval.
     * @param onFailure Callback to be executed on failure.
     */
    fun getPosts(onSuccess: (List<Post>) -> Unit, onFailure: (Exception) -> Unit) {
        firestoreService.getPosts(onSuccess, onFailure)
    }

    /**
     * Retrieves a list of Posts for a specific user from the data base using the injected FirestoreService.
     * @param userId The ID of the user for whom to retrieve posts.
     * @param onSuccess Callback to be executed on successful retrieval.
     * @param onFailure Callback to be executed on failure.
     */
    fun getUserPosts(userId: String, onSuccess: (List<Post>) -> Unit, onFailure: (Exception) -> Unit) {
        firestoreService.getUserPosts(userId, onSuccess, onFailure)
    }
}