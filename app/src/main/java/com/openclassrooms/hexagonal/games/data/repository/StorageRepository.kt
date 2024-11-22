package com.openclassrooms.hexagonal.games.data.repository

import android.net.Uri
import com.openclassrooms.hexagonal.games.data.service.storage.FirebaseStorageService
import javax.inject.Inject

/**
 * Repository class responsible for handling storage-related operations.
 */
class StorageRepository @Inject constructor(private val firebaseStorageService: FirebaseStorageService) {

    /**
     * Uploads an image to Firebase Storage.
     * @param imageUri The URI of the image to be uploaded.
     * @param onSuccess Callback to be invoked upon successful upload.
     * @param onError Callback to be invoked upon upload failure.
     */
    fun uploadImage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firebaseStorageService.uploadImage(
            imageUri = imageUri,
            onSuccess = onSuccess,
            onError = onError
        )
    }


}