package com.openclassrooms.hexagonal.games.repositoryUnitTest

import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.data.service.firestore.FirestoreService
import com.openclassrooms.hexagonal.games.domain.model.User
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the UserStoreRepository class.
 */
class UserStoreRepositoryTest {

    private lateinit var firestoreService: FirestoreService
    private lateinit var userStoreRepository: UserStoreRepository

    @Before
    fun setUp() {
        // Initialize the mocks
        firestoreService = mockk()
        userStoreRepository = UserStoreRepository(firestoreService)
    }

    /**
     * Test the addUser method of the UserStoreRepository.
     */
    @Test
    fun addUser_shouldInvokeOnSuccessWhenUserIsAddedSuccessfully() {
        // Arrange
        val user = User(id = "123", firstname = "John", lastname = "Doe")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock FirestoreService's addUser method
        every { firestoreService.addUser(user, onSuccess, onFailure) } answers {
            onSuccess() // Simulate successful user addition
        }

        // Act
        userStoreRepository.addUser(user, onSuccess, onFailure)

        // Assert
        verify { onSuccess() }
        verify(exactly = 0) { onFailure(any()) }
    }


    /**
     * Test the addUser method of the UserStoreRepository.
     */
    @Test
    fun addUser_shouldInvokeOnFailureWhenAddingUserFails() {
        // Arrange
        val user = User(id = "123", firstname = "John", lastname = "Doe")
        val exception = Exception("Failed to add user")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock FirestoreService's addUser method to simulate failure
        every { firestoreService.addUser(user, onSuccess, onFailure) } answers {
            onFailure(exception) // Simulate failure
        }

        // Act
        userStoreRepository.addUser(user, onSuccess, onFailure)

        // Assert
        verify { onFailure(exception) }
        verify(exactly = 0) { onSuccess() }
    }

    /**
     * Test the getUser method of the UserStoreRepository.
     */
    @Test
    fun getUser_shouldInvokeOnSuccessWhenUserIsRetrievedSuccessfully() {
        // Arrange
        val userId = "123"
        val user = User(id = "123", firstname = "John", lastname = "Doe")
        val onSuccess: (User) -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock FirestoreService's getUserById method
        every { firestoreService.getUserById(userId, onSuccess, onFailure) } answers {
            onSuccess(user) // Simulate successful user retrieval
        }

        // Act
        userStoreRepository.getUser(userId, onSuccess, onFailure)

        // Assert
        verify { onSuccess(user) }
        verify(exactly = 0) { onFailure(any()) }
    }

    /**
     * Test the getUser method of the UserStoreRepository.
     */
    @Test
    fun getUser_shouldInvokeOnFailureWhenUserRetrievalFails() {
        // Arrange
        val userId = "123"
        val exception = Exception("User not found")
        val onSuccess: (User) -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock FirestoreService's getUserById method to simulate failure
        every { firestoreService.getUserById(userId, onSuccess, onFailure) } answers {
            onFailure(exception) // Simulate failure
        }

        // Act
        userStoreRepository.getUser(userId, onSuccess, onFailure)

        // Assert
        verify { onFailure(exception) }
        verify(exactly = 0) { onSuccess(any()) }
    }

    /**
     * Test the deleteUser method of the UserStoreRepository.
     */
    @Test
    fun deleteUser_shouldInvokeOnSuccessWhenUserIsDeletedSuccessfully() {
        // Arrange
        val userId = "123"
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock FirestoreService's deleteUser method
        every { firestoreService.deleteUser(userId, onSuccess, onFailure) } answers {
            onSuccess() // Simulate successful user deletion
        }

        // Act
        userStoreRepository.deleteUser(userId, onSuccess, onFailure)

        // Assert
        verify { onSuccess() }
        verify(exactly = 0) { onFailure(any()) }
    }

    /**
     * Test the deleteUser method of the UserStoreRepository.
     */
    @Test
    fun deleteUser_shouldInvokeOnFailureWhenUserDeletionFails() {
        // Arrange
        val userId = "123"
        val exception = Exception("Failed to delete user")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Mock FirestoreService's deleteUser method to simulate failure
        every { firestoreService.deleteUser(userId, onSuccess, onFailure) } answers {
            onFailure(exception) // Simulate failure
        }

        // Act
        userStoreRepository.deleteUser(userId, onSuccess, onFailure)

        // Assert
        verify { onFailure(exception) }
        verify(exactly = 0) { onSuccess() }
    }

    /**
     * Tear down the test environment.
     */
    @After
    fun tearDown() {
        // Clear the mocks after each test
        clearMocks(firestoreService)
    }
}