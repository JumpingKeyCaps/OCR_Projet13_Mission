package com.openclassrooms.hexagonal.games.di

import android.app.NotificationManager
import android.content.Context
import com.openclassrooms.hexagonal.games.data.repository.AuthRepository
import com.openclassrooms.hexagonal.games.data.repository.CommentStoreRepository
import com.openclassrooms.hexagonal.games.data.repository.PostStoreRepository
import com.openclassrooms.hexagonal.games.data.repository.StorageRepository
import com.openclassrooms.hexagonal.games.data.repository.UserStoreRepository
import com.openclassrooms.hexagonal.games.data.service.PostApi
import com.openclassrooms.hexagonal.games.data.service.PostFakeApi
import com.openclassrooms.hexagonal.games.data.service.authentication.FirebaseAuthService
import com.openclassrooms.hexagonal.games.data.service.firestore.FirestoreService
import com.openclassrooms.hexagonal.games.data.service.storage.FirebaseStorageService
import com.openclassrooms.hexagonal.games.screen.settings.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This class acts as a Dagger Hilt module, responsible for providing dependencies to other parts of the application.
 * It's installed in the SingletonComponent, ensuring that dependencies provided by this module are created only once
 * and remain available throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
  /**
   * Provides a Singleton instance of PostApi using a PostFakeApi implementation for testing purposes.
   * This means that whenever a dependency on PostApi is requested, the same instance of PostFakeApi will be used
   * throughout the application, ensuring consistent data for testing scenarios.
   *
   * @return A Singleton instance of PostFakeApi.
   */
  @Provides
  @Singleton
  fun providePostApi(): PostApi {
    return PostFakeApi()
  }


  /**
   * Provide a singleton instance of Authentication Repository
   * @param authService The Authentication Service
   * @return The Authentication Repository
   */
  @Provides
  @Singleton
  fun provideAuthRepository(authService: FirebaseAuthService): AuthRepository {
    return AuthRepository(authService)
  }

  /**
   * Provides a Singleton instance of FirebaseAuthService.
   * @return A Singleton instance of FirebaseAuthService.
   */
  @Provides
  @Singleton
  fun provideFirebaseAuthService(): FirebaseAuthService {
    return FirebaseAuthService()
  }


  /**
   * Provides a Singleton instance of FirestoreService.
   * @return A Singleton instance of FirestoreService.
   */
  @Provides
  @Singleton
  fun provideFirestoreService(): FirestoreService {
    return FirestoreService()
  }


  /**
   * Provides a Singleton instance of FirebaseStorageService.
   * @return A Singleton instance of FirebaseStorageService.
   */
  @Provides
  @Singleton
  fun provideFireBaseStorageService(): FirebaseStorageService {
    return FirebaseStorageService()
  }

  /**
   * Provides a Singleton instance of UserStoreRepository.
   * @param storeService The FirestoreService instance.
   * @return A Singleton instance of UserStoreRepository.
   */
  @Provides
  @Singleton
  fun provideUserStoreRepository(storeService: FirestoreService): UserStoreRepository {
    return UserStoreRepository(storeService)
  }


  /**
   * Provides a Singleton instance of PostStoreRepository.
   * @param storeService The FirestoreService instance.
   * @return A Singleton instance of PostStoreRepository.
   */
  @Provides
  @Singleton
  fun providePostStoreRepository(storeService: FirestoreService): PostStoreRepository {
    return PostStoreRepository(storeService)
  }


  /**
   * Provides a Singleton instance of FirebaseStorageService.
   * @param storageService The FirebasestorageService instance.
   * @return A Singleton instance of FirebaseStorageRepository.
   *
   */
  @Provides
  @Singleton
  fun provideStorageRepository(storageService: FirebaseStorageService): StorageRepository {
    return StorageRepository(storageService)
  }

  @Provides
  @Singleton
  fun provideCommentStoreRepository(storeService: FirestoreService): CommentStoreRepository {
    return CommentStoreRepository(storeService)
  }


  //Pref manager and notification manager providers

  @Provides
  @Singleton
  fun providePreferencesManager(@ApplicationContext context: Context): PreferenceManager {
    return PreferenceManager(context)
  }

  @Provides
  @Singleton
  fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }



}
