package com.openclassrooms.hexagonal.games.repositoryUnitTest

import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import com.openclassrooms.hexagonal.games.data.service.authentication.FirebaseAuthService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


/**
 * Unit tests for the AuthRepository class.
 */
class AuthRepositoryTest {

    // Mock the FirebaseAuthService
    private lateinit var mockAuthService: FirebaseAuthService

    // Repository under test
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        mockAuthService = mockk()
        authRepository = AuthRepository(mockAuthService)
    }


    /**
     * Test the registerUser method of the AuthRepository.
     */
    @Test
    fun registerUserShouldReturnAResultWithFirebaseUser() = runTest {
        // Mock data
        val email = "test@example.com"
        val password = "password123"
        val mockFirebaseUser: FirebaseUser = mockk()
        val expectedResult = Result.success(mockFirebaseUser)
        // Mock behavior
        coEvery { mockAuthService.registerUser(email, password) } returns expectedResult
        // Call the method
        val result = authRepository.registerUser(email, password).first()
        // Assert the result
        assertEquals(expectedResult, result)
        // Verify the interaction
        coVerify { mockAuthService.registerUser(email, password) }
    }

    /**
     * Test the signInUser method of the AuthRepository.
     */
    @Test
    fun signInUserShouldReturnAResultWithFirebaseUser() = runTest {
        // Mock data
        val email = "test@example.com"
        val password = "password123"
        val mockFirebaseUser: FirebaseUser = mockk()
        val expectedResult = Result.success(mockFirebaseUser)
        // Mock behavior
        coEvery { mockAuthService.signInUser(email, password) } returns expectedResult
        // Call the method
        val result = authRepository.signInUser(email, password).first()
        // Assert the result
        assertEquals(expectedResult, result)
        // Verify the interaction
        coVerify { mockAuthService.signInUser(email, password) }
    }

    /**
     * Test the signOutUser method of the AuthRepository.
     */
    @Test
    fun signOutUserShouldReturnAResultWithUnit() = runTest {
        // Mock data
        val expectedResult = Result.success(Unit)
        // Mock behavior
        coEvery { mockAuthService.signOutUser() } returns expectedResult
        // Call the method
        val result = authRepository.signOutUser().first()
        // Assert the result
        assertEquals(expectedResult, result)
        // Verify the interaction
        coVerify { mockAuthService.signOutUser() }
    }

    /**
     * Test the deleteUserAccount method of the AuthRepository.
     *
     */
    @Test
    fun deleteUserAccountShouldReturnAResultWithUnit() = runTest {
        val mockFirebaseUser: FirebaseUser = mockk()
        val expectedResult = Result.success(Unit)
        coEvery { mockAuthService.deleteUserAccount(mockFirebaseUser) } returns expectedResult
        val result = authRepository.deleteUserAccount(mockFirebaseUser).first()
        assertEquals(expectedResult, result)
        coVerify { mockAuthService.deleteUserAccount(mockFirebaseUser) }
    }

    /**
     * Test the sendPasswordResetEmail method of the AuthRepository.
     */
    @Test
    fun sendPasswordResetEmailShouldReturnAResultWithUnit() = runTest {
        val email = "test@example.com"
        val expectedResult = Result.success(Unit)
        coEvery { mockAuthService.sendPasswordResetEmail(email) } returns expectedResult
        val result = authRepository.sendPasswordResetEmail(email).first()
        assertEquals(expectedResult, result)
        coVerify { mockAuthService.sendPasswordResetEmail(email) }
    }



}