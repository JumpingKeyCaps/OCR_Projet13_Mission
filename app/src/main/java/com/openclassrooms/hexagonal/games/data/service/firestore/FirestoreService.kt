package com.openclassrooms.hexagonal.games.data.service.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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
            .addOnSuccessListener {
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
        val documentReference = db.collection("posts").document() // Crée un document avec un ID généré
        val postWithId = post.copy(id = documentReference.id) // Copie le post avec l'ID Firestore assigné
        documentReference
            .set(postWithId) // Enregistre le post dans Firestore
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Récupère un post par son ID.
     * @param postId L'ID du post.
     * @param onSuccess La fonction à exécuter en cas de succès.
     * @param onFailure La fonction à exécuter en cas d'échec.
     */
    fun getPostById(postId: String, onSuccess: (Post?) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.toObject(Post::class.java))
                } else {
                    onSuccess(null) // Document does not exist
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Observe All posts in Firestore (realtime)
     * @return a Flow of List<Post>
     */
    fun observePosts(): Flow<List<Post>> = callbackFlow {
        val listenerRegistration = db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Tri par timestamp, décroissant (les plus récents en premier)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    // Envoie une erreur au Flow
                    close(e)
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    val posts = querySnapshot.documents.mapNotNull { it.toObject(Post::class.java) }
                    trySend(posts) // Envoie les données au Flow
                }
            }
        // Supprime le listener lorsque le Flow est annulé
        awaitClose { listenerRegistration.remove() }
    }


    //----- Comment stuff
    /**
     * Add a comment to a post.
     * @param postId The ID of the post to which the comment will be added.
     * @param comment The comment to be added.
     * @param onSuccess Callback to be invoked upon successful addition of the comment.
     * @param onFailure Callback to be invoked upon failure of the addition of the comment.
     */
    fun addCommentToPost(postId: String, comment: Comment, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Obtenir une référence à la collection de commentaires du post
        val commentsCollectionRef = db.collection("posts")
            .document(postId)
            .collection("comments")

        // Générer un nouvel ID pour le commentaire
        val commentId = commentsCollectionRef.document().id // Génère un ID unique

        // Ajouter l'ID généré au commentaire
        val commentWithId = comment.copy(id = commentId)

        // Ajouter le commentaire avec l'ID dans Firestore
        commentsCollectionRef.document(commentId)
            .set(commentWithId)
            .addOnSuccessListener {
                onSuccess() // Si l'ajout réussit
            }
            .addOnFailureListener { exception ->
                onFailure(exception) // Si l'ajout échoue
            }
    }

    /**
     * Observe comments for a specific post.
     * @param postId The ID of the post for which comments will be observed.
     * @return A Flow emitting a list of comments.
     */
    fun observeComments(postId: String): Flow<List<Comment>> = callbackFlow {
        val listenerRegistration = db.collection("posts")
            .document(postId)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.ASCENDING) // Tri par date croissante (les plus anciens en premier)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    // Envoie une erreur au Flow
                    close(e)
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    val comments = querySnapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
                    trySend(comments) // Envoie les données au Flow
                }
            }
        // Supprime le listener lorsque le Flow est annulé
        awaitClose { listenerRegistration.remove() }
    }

}