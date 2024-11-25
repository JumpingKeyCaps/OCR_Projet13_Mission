package com.openclassrooms.hexagonal.games.repositoryUnitTest

import com.openclassrooms.hexagonal.games.data.repository.PostStoreRepository
import com.openclassrooms.hexagonal.games.data.service.firestore.FirestoreService
import com.openclassrooms.hexagonal.games.domain.model.Post
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
 * Unit tests for the PostStoreRepository class.
 */
class PostStoreRepositoryTest {

    // Mock the FirestoreService
    private lateinit var mockFirestoreService: FirestoreService

    // Repository under test
    private lateinit var postStoreRepository: PostStoreRepository

    @Before
    fun setUp() {
        mockFirestoreService = mockk()
        postStoreRepository = PostStoreRepository(mockFirestoreService)
    }

    /**
     * Test the addPost method of the PostStoreRepository.
     */
    @Test
    fun addPostShouldInvokeOnSuccessWhenPostIsAddedSuccessfully() = runTest {
        // Mock data
        val post = Post("1", "Post content", "author123")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock behavior
        coEvery { mockFirestoreService.addPost(post, onSuccess, onFailure) } answers {
            onSuccess() // Simulate success callback
        }

        // Call the method
        postStoreRepository.addPost(post, onSuccess, onFailure)

        // Verify that onSuccess is invoked
        verify { onSuccess() }
        verify(exactly = 0) { onFailure(any()) } // Ensure onFailure is not invoked
    }

    /**
     * Test the addPost method of the PostStoreRepository.
     */
    @Test
    fun addPostShouldInvokeOnFailureWhenExceptionIsThrown() = runTest {
        // Mock data
        val post = Post("1", "Post content", "author123")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock behavior to simulate failure
        coEvery { mockFirestoreService.addPost(post, onSuccess, onFailure) } answers {
            onFailure(Exception("Network error")) // Simulate failure callback
        }

        // Call the method
        postStoreRepository.addPost(post, onSuccess, onFailure)

        // Verify that onFailure is invoked
        verify { onFailure(any()) }
        verify(exactly = 0) { onSuccess() } // Ensure onSuccess is not invoked
    }

    /**
     * Test the observePosts method of the PostStoreRepository.
     */
    @Test
    fun observePostsShouldReturnAFlowOfPosts() = runTest {
        // Mock data
        val posts = listOf(Post("1", "Post content", "author123"), Post("2", "Another post", "author456"))

        // Mock behavior
        coEvery { mockFirestoreService.observePosts() } returns flow {
            emit(posts) // Emit the list of posts
        }

        // Call the method and collect the emitted value from the Flow
        val result = postStoreRepository.observePosts().first()

        // Assert the result
        assertEquals(posts, result)

        // Verify the interaction
        coVerify { mockFirestoreService.observePosts() }
    }

    /**
     * Test the getPostById method of the PostStoreRepository.
     */
    @Test
    fun getPostByIdShouldReturnPostWhenFound() = runTest {
        // Mock data
        val postId = "1"
        val post = Post(postId, "Post content", "author123")
        val onSuccess: (Post?) -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock behavior to simulate success
        coEvery { mockFirestoreService.getPostById(postId, onSuccess, onFailure) } answers {
            onSuccess(post) // Simulate success callback with a post
        }

        // Call the method
        postStoreRepository.getPostById(postId, onSuccess, onFailure)

        // Verify that onSuccess is invoked with the correct post
        verify { onSuccess(post) }
        verify(exactly = 0) { onFailure(any()) } // Ensure onFailure is not invoked
    }

    /**
     * Test the getPostById method of the PostStoreRepository.
     */
    @Test
    fun getPostByIdShouldInvokeOnFailureWhenPostNotFound() = runTest {
        // Mock data
        val postId = "1"
        val post: Post? = null
        val onSuccess: (Post?) -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock behavior to simulate failure
        coEvery { mockFirestoreService.getPostById(postId, onSuccess, onFailure) } answers {
            onFailure(Exception("Post not found")) // Simulate failure callback
        }

        // Call the method
        postStoreRepository.getPostById(postId, onSuccess, onFailure)

        // Verify that onFailure is invoked
        verify { onFailure(any()) }
        verify(exactly = 0) { onSuccess(any()) } // Ensure onSuccess is not invoked
    }
}