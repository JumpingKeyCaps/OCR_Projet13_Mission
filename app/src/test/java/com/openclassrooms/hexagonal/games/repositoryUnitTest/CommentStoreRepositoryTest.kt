package com.openclassrooms.hexagonal.games.repositoryUnitTest

import com.openclassrooms.hexagonal.games.data.repository.CommentStoreRepository
import com.openclassrooms.hexagonal.games.data.service.firestore.FirestoreService
import com.openclassrooms.hexagonal.games.domain.model.Comment
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


/**
 * Unit tests for the CommentStoreRepository class.
 */
class CommentStoreRepositoryTest {

    // Mock the FirestoreService
    private lateinit var mockFirestoreService: FirestoreService

    // Repository under test
    private lateinit var commentStoreRepository: CommentStoreRepository

    @Before
    fun setUp() {
        mockFirestoreService = mockk()
        commentStoreRepository = CommentStoreRepository(mockFirestoreService)
    }

    /**
     * Test the addCommentToPost method of the CommentStoreRepository.
     */
    @Test
    fun addCommentToPostShouldInvokeOnSuccessWhenCommentIsAddedSuccessfully() = runTest {
        // Mock data
        val postId = "post123"
        val comment = Comment("Great post!")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock behavior
        coEvery { mockFirestoreService.addCommentToPost(postId, comment, onSuccess, onFailure) } answers {
            onSuccess() // Simulate success callback
        }

        // Call the method
        commentStoreRepository.addCommentToPost(postId, comment, onSuccess, onFailure)

        // Verify that onSuccess is invoked
        verify { onSuccess() }
        verify(exactly = 0) { onFailure(any()) } // Ensure onFailure is not invoked
    }

    /**
     * Test the addCommentToPost method of the CommentStoreRepository.
     */
    @Test
    fun addCommentToPostShouldInvokeOnFailureWhenExceptionIsThrown() = runTest {
        // Mock data
        val postId = "post123"
        val comment = Comment("Great post!")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock behavior to simulate failure
        coEvery { mockFirestoreService.addCommentToPost(postId, comment, onSuccess, onFailure) } answers {
            onFailure(Exception("Network error")) // Simulate failure callback
        }

        // Call the method
        commentStoreRepository.addCommentToPost(postId, comment, onSuccess, onFailure)

        // Verify that onFailure is invoked
        verify { onFailure(any()) }
        verify(exactly = 0) { onSuccess() } // Ensure onSuccess is not invoked
    }

    /**
     * Test the observeComments method of the CommentStoreRepository.
     */
    @Test
    fun observeCommentsShouldReturnAFlowOfCommentsForAPost() = runTest {
        // Mock data
        val postId = "post123"
        val comments = listOf(Comment("Great post!"), Comment("Nice article!"))

        // Mock behavior
        coEvery { mockFirestoreService.observeComments(postId) } returns flow {
            emit(comments) // Emit the list of comments
        }

        // Call the method and collect the emitted value from the Flow
        val result = commentStoreRepository.observeComments(postId).first()

        // Assert the result
        assertEquals(comments, result)

        // Verify the interaction
        coVerify { mockFirestoreService.observeComments(postId) }
    }
}