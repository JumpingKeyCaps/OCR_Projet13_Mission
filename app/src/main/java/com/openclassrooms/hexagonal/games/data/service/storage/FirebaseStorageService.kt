package com.openclassrooms.hexagonal.games.data.service.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * FirebaseStorageService is a class responsible for handling Firebase Storage operations.
 */
class FirebaseStorageService {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

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
        // Crée une référence pour stocker le fichier
        val fileReference = storageReference.child("images/${System.currentTimeMillis()}.jpg")

        fileReference.putFile(imageUri)
            .addOnSuccessListener {
                // Récupérer l'URL téléchargeable après le succès
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}