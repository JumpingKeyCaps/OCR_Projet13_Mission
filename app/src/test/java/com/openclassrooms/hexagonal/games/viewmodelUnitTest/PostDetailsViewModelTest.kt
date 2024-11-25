package com.openclassrooms.hexagonal.games.viewmodelUnitTest

import com.openclassrooms.hexagonal.games.data.repository.CommentStoreRepository
import com.openclassrooms.hexagonal.games.data.repository.PostStoreRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.screen.postdetails.PostDetailsViewModel
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.invoke
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the PostDetailsViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailsViewModelTest {

    private lateinit var postDetailsViewModel: PostDetailsViewModel
    private lateinit var postStoreRepository: PostStoreRepository
    private lateinit var commentStoreRepository: CommentStoreRepository
    private lateinit var userStoreRepository: UserStoreRepository

    @Before
    fun setUp() {
        // Set Main dispatcher to TestDispatcher
        Dispatchers.setMain(TestCoroutineDispatcher())

        // Initialize repositories as mocks
        postStoreRepository = mockk(relaxed = true)
        commentStoreRepository = mockk(relaxed = true)
        userStoreRepository = mockk(relaxed = true)

        // Initialize the ViewModel
        postDetailsViewModel = PostDetailsViewModel(
            postStoreRepository,
            commentStoreRepository,
            userStoreRepository
        )
    }

    @After
    fun tearDown() {
        // Reset Main dispatcher
        Dispatchers.resetMain()
        clearAllMocks()
    }


    /**
     * Test case for the observeComments method in the PostDetailsViewModel.
     */
    @Test
    fun observeComments_should_call_observeComments_in_commentStoreRepository() = runTest {
        // Arrange
        val testScope = this // Use the runTest scope
        val postId = "testPostId"
        val commentsFlow = flowOf(listOf(Comment(id = "1", content = "Test", authorId = "user1", authorName = "User One")))

        coEvery { commentStoreRepository.observeComments(postId) } returns commentsFlow

        val testViewModel = PostDetailsViewModel(postStoreRepository,commentStoreRepository, userStoreRepository)

        // Act
        testViewModel.observeComments(postId)

        // Assert
        coVerify { commentStoreRepository.observeComments(postId) }
    }


    /**
     * Test case for the addCommentToPost method in the PostDetailsViewModel.
     */
    @Test
    fun addCommentToPost_should_call_getUser_and_addCommentToPost() = runTest {
        // Arrange
        val testPostId = "testPostId"
        val testComment = Comment(
            id = "1",
            content = "This is a test comment",
            authorId = "user1",
            authorName = ""
        )
        val mockUser = User(
            id = "user1",
            firstname = "John",
            lastname = "Doe"
        )

        // Mocking the getUser method
        coEvery {
            userStoreRepository.getUser(
                userId = testComment.authorId,
                onSuccess = captureLambda(),
                onFailure = any()
            )
        } answers {
            lambda<(User) -> Unit>().invoke(mockUser) // Simulate onSuccess callback
        }

        // Mocking the addCommentToPost method
        coEvery {
            commentStoreRepository.addCommentToPost(
                postId = testPostId,
                comment = any(),
                onSuccess = any(),
                onFailure = any()
            )
        } just Runs

        val testViewModel = PostDetailsViewModel(postStoreRepository, commentStoreRepository, userStoreRepository)

        // Act
        testViewModel.addCommentToPost(testPostId, testComment)

        // Assert
        coVerify {
            userStoreRepository.getUser(
                userId = testComment.authorId,
                onSuccess = any(),
                onFailure = any()
            )
            commentStoreRepository.addCommentToPost(
                postId = testPostId,
                comment = testComment.copy(authorName = "${mockUser.firstname} ${mockUser.lastname} "),
                onSuccess = any(),
                onFailure = any()
            )
        }
    }



}