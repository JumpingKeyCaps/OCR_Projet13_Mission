package com.openclassrooms.hexagonal.games.data.service.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User

/**
 * Service pour interagir avec Firestore.
 */
class FirestoreService {

    private val db = FirebaseFirestore.getInstance("hexagonaldb")

    // ---------- User stuff

    /**
     * Ajoute un utilisateur à Firestore.
     * @param user L'utilisateur à ajouter.
     * @param onSuccess La fonction à exécuter en cas de succès.
     * @param onFailure La fonction à exécuter en cas d'échec.
     */
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(user.id)  // set user id
            .set(user) // create user or updtate if already exist
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }



    /**
     * Récupère un utilisateur par son ID.
     * @param uid L'ID de l'utilisateur.
     * @param onSuccess La fonction à exécuter en cas de succès.
     * @param onFailure La fonction à exécuter en cas d'échec.
     */
    fun getUserById(uid: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(uid)  // On spécifie l'ID du document (ici l'UID de l'utilisateur)
            .get()  // On récupère le document
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)  // Convertir le document en objet User
                    if (user != null) {
                        onSuccess(user)  // Retourne l'utilisateur via le callback onSuccess
                    } else {
                        onFailure(Exception("User data is null"))  // Gestion de l'erreur si l'utilisateur est null
                    }
                } else {
                    onFailure(Exception("User not found"))  // L'utilisateur n'existe pas dans Firestore
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)  // En cas d'erreur, retourne l'exception
            }
    }


    /**
     * Supprime un utilisateur par son ID.
     * @param uid L'ID de l'utilisateur.
     * @param onSuccess La fonction à exécuter en cas de succès.
     * @param onFailure La fonction à exécuter en cas d'échec.
     */
    fun deleteUser(uid: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(uid)  // Cible le document de l'utilisateur avec son UID
            .delete()  // Supprime le document de l'utilisateur
            .addOnSuccessListener {
                onSuccess()  // Appelle onSuccess si la suppression réussit
            }
            .addOnFailureListener { e ->
                onFailure(e)  // En cas d'échec, appelle onFailure avec l'exception
            }
    }



    // ---------- Post stuff

    /**
     * Ajoute un post à Firestore.
     * @param post Le post à ajouter.
     * @param onSuccess La fonction à exécuter en cas de succès.
     * @param onFailure La fonction à exécuter en cas d'échec.
     */
    fun addPost(post: Post, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Récupère tous les posts.
     * @param onSuccess La fonction à exécuter en cas de succès.
     * @param onFailure La fonction à exécuter en cas d'échec.
     */
    fun getPosts(onSuccess: (List<Post>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.documents.mapNotNull { it.toObject(Post::class.java) }
                onSuccess(posts)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Récupère les posts d'un utilisateur spécifique.
     * @param userId L'ID de l'utilisateur.
     * @param onSuccess La fonction à exécuter en cas de succès.
     * @param onFailure La fonction à exécuter en cas d'échec.
     */
    fun getUserPosts(userId: String, onSuccess: (List<Post>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.documents.mapNotNull { it.toObject(Post::class.java) }
                onSuccess(posts)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }




}