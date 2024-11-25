package com.openclassrooms.hexagonal.games.viewmodelUnitTest

import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.data.service.authentication.FirebaseAuthService
import com.openclassrooms.hexagonal.games.screen.account.MyAccountViewModel
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MyAccountViewModelTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var userStoreRepository: UserStoreRepository


    @Mock
    private lateinit var user: FirebaseUser

    private lateinit var viewModel: MyAccountViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        // Set the Main dispatcher to a test dispatcher
        Dispatchers.setMain(UnconfinedTestDispatcher()) // Use UnconfinedTestDispatcher for coroutine context


        // Initialize the ViewModel with mocked repositories
        viewModel = MyAccountViewModel(authRepository, userStoreRepository)



    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // Reset the Main dispatcher to the original Main dispatcher
        Dispatchers.resetMain()
    }

    /**
     * Test the signOutUser method of the MyAccountViewModel.
     */
    @Test
    fun signOutUser_shouldCallAuthRepositorySignOut() = runTest {
        // Arrange
        Mockito.`when`(authRepository.signOutUser()).thenReturn(flowOf(Result.success(Unit)))

        // Act
        viewModel.signOutUser()

        // Assert
        Mockito.verify(authRepository).signOutUser() // Verify that signOutUser was called on the repository
    }

    /**
     * Test that removeUserAuth calls deleteUserAccount on the repository
     */
    @Test
    fun removeUserAuth_shouldCallDeleteUserAccount() = runTest {
        // Arrange: Mock the deleteUserAccount to return a successful flow
        Mockito.`when`(authRepository.deleteUserAccount(user)).thenReturn(flowOf(Result.success(Unit)))

        // Act: Call the removeUserAuth method
        viewModel.removeUserAuth(user)

        // Assert: Verify that deleteUserAccount was called once with the correct user
        Mockito.verify(authRepository, Mockito.times(1)).deleteUserAccount(user)
    }
}