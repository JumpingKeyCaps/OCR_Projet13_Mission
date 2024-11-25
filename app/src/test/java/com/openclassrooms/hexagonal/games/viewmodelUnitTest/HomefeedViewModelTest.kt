package com.openclassrooms.hexagonal.games.viewmodelUnitTest

import com.openclassrooms.hexagonal.games.data.repository.PostStoreRepository
import com.openclassrooms.hexagonal.games.screen.homefeed.HomefeedViewModel
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the HomefeedViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomefeedViewModelTest {

    private lateinit var homefeedViewModel: HomefeedViewModel
    private lateinit var postStoreRepository: PostStoreRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        // Initialize the repository as a mock
        postStoreRepository = mockk(relaxed = true)

        // Set the Main dispatcher to the test dispatcher for coroutine control
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel
        homefeedViewModel = HomefeedViewModel(postStoreRepository)
    }

    @After
    fun tearDown() {
        // Reset the dispatcher and clean up mocks
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearAllMocks()
    }

    /**
     * Test case for the observePosts method in the HomefeedViewModel.
     */
    @Test
    fun observePosts_should_call_observePosts_in_repository() = runTest {
        // Verify that observePosts was called
        coVerify { postStoreRepository.observePosts() }
    }
}