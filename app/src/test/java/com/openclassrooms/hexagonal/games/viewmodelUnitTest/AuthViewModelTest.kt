package com.openclassrooms.hexagonal.games.viewmodelUnitTest

import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.screen.auth.AuthViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


/**
 * Unit tests for the AuthViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserStoreRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        // Initialize the repositories as mocks
        authRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)

        // Set the Main dispatcher to the test dispatcher for coroutine control
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel
        authViewModel = AuthViewModel(authRepository, userRepository)
    }

    @After
    fun tearDown() {
        // Reset the dispatcher and clean up mocks
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearAllMocks()
    }

    /**
     * Test case for the signUpUser method in the AuthViewModel.
     */
    @Test
    fun signUpUser_should_call_authRepository_registerUser() = runTest {
        // Arrange: Create sample input data for sign-up
        val email = "test@example.com"
        val password = "password123"
        val firstName = "John"
        val lastName = "Doe"
        val firebaseUser = mockk<FirebaseUser>()

        // Mock the behavior of getUid for FirebaseUser
        every { firebaseUser.uid } returns "mockUid"  // Mock getUid()

        // Mock the result of the registerUser method
        coEvery { authRepository.registerUser(email, password) } returns flow {
            emit(Result.success(firebaseUser))
        }

        // Act: Call the signUpUser method in the ViewModel
        authViewModel.signUpUser(email, password, firstName, lastName)

        // Advance the virtual time to make sure the coroutine runs
        advanceTimeBy(1000)

        // Assert: Verify that authRepository.registerUser was called with correct arguments
        coVerify { authRepository.registerUser(email, password) }
    }

    /**
     * Test case for the signInUser method in the AuthViewModel.
     */
    @Test
    fun signInUser_should_call_authRepository_signInUser() = runTest {
        // Arrange: Create sample input data for sign-in
        val email = "test@example.com"
        val password = "password123"
        val firebaseUser = mockk<FirebaseUser>()

        // Mock the result of the signInUser method
        coEvery { authRepository.signInUser(email, password) } returns flow {
            emit(Result.success(firebaseUser))
        }

        // Act: Call the signInUser method in the ViewModel
        authViewModel.signInUser(email, password)

        // Advance the virtual time to make sure the coroutine runs
        advanceTimeBy(1000)

        // Assert: Verify that authRepository.signInUser was called with correct arguments
        coVerify { authRepository.signInUser(email, password) }

    }

    /**
     * Test case for the sendPasswordResetEmail method in the AuthViewModel.
     */
    @Test
    fun sendPasswordResetEmail_should_call_authRepository_sendPasswordResetEmail() = runTest {
        // Arrange: Create sample input data for password reset
        val email = "test@example.com"

        // Mock the result of the sendPasswordResetEmail method
        coEvery { authRepository.sendPasswordResetEmail(email) } returns flow {
            emit(Result.success(Unit))
        }

        // Act: Call the sendPasswordResetEmail method in the ViewModel
        authViewModel.sendPasswordResetEmail(email)

        // Advance the virtual time to make sure the coroutine runs
        advanceTimeBy(1000)

        // Assert: Verify that authRepository.sendPasswordResetEmail was called with correct arguments
        coVerify { authRepository.sendPasswordResetEmail(email) }
    }


}