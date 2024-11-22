package com.openclassrooms.hexagonal.games.domain.model

import java.io.Serializable

/**
 *  Class representing a comment in the application.
 *
 *  @param id The unique identifier of the comment.
 *  @param authorId The identifier of the author of the comment.
 *  @param authorName The name of the author of the comment.
 *  @param content The content of the comment.
 *  @param timestamp the timestamp of the comment.
 */
data class Comment(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
): Serializable