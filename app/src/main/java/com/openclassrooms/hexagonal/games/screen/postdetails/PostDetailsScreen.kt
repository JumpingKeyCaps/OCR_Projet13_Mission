package com.openclassrooms.hexagonal.games.screen.postdetails

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Composable function to display the screen details of a post.
 * @param userAuthId The ID of the currently authenticated user.
 * @param postId The ID of the post to display.
 * @param onBackClick Callback to handle the back button click.
 * @param viewModel The ViewModel for managing the screen data.
 */
@Composable
fun PostDetailsScreen(
    userAuthId: String?,
    postId:String,
    onBackClick: () -> Unit,
    viewModel: PostDetailsViewModel = hiltViewModel()) {

    val post by viewModel.post.collectAsState()
    val comments by viewModel.comments.collectAsState()

    // Load the post when the screen is launched + commentary
    LaunchedEffect(postId) {
        Log.d("PostDetailsScreen", "Loading post with ID: $postId")
        viewModel.loadPost(postId)
        viewModel.observeComments(postId)
    }


    // Display the post details
    if (post != null) {
        Box(modifier = Modifier.fillMaxSize().padding(0.dp, 0.dp, 0.dp, 0.dp)){
            //Back arrow
            IconButton(onClick = {
                onBackClick()
            },
                modifier = Modifier.padding(16.dp, 6.dp, 0.dp, 0.dp).align(Alignment.TopStart)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.Black,
                    contentDescription = stringResource(id = R.string.contentDescription_go_back)
                )
            }

            //Content
            PostContent(post = post,
                comments = comments,
                onAddComment = { commentText ->
                    if(userAuthId!=null){
                        //create the comment
                        val comment = Comment(
                            authorId = userAuthId,
                            content = commentText,
                            timestamp = System.currentTimeMillis()
                        )
                        // add the comment to the post
                        viewModel.addCommentToPost(postId, comment)
                    }
                },
                commentCount = comments.size,
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(18.dp, 60.dp, 18.dp, 0.dp))
        }
    } else {
        // Loading or error state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading post details...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


/**
 * Composable function to display the content of a post.
 * @param post The post to display.
 * @param onAddComment Callback to handle adding a comment.
 * @param commentCount The number of comments.
 * @param modifier Modifier for styling and layout.
 */
@Composable
fun PostContent(post: Post?,comments: List<Comment>,onAddComment: (String) -> Unit,commentCount: Int, modifier: Modifier = Modifier){
    LazyColumn(
        modifier = modifier
    ) {
        // -------------- Content of the post
        item {
            // Post Picture
            if (!post!!.photoUrl.isNullOrEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp)), // Rounded corners
                    model = post.photoUrl,
                    contentDescription = "Post Image",
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Post Title
            Text(
                text = post.title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Author Name and Comment Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Author: ${post.author?.firstname ?: "Anonymous"} ${post.author?.lastname ?: " "}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "$commentCount commentaires",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Post Description
            Text(
                text = post.description ?: "No description available.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth().padding(bottom = 66.dp)
            )
        }
        // --------------  Titre des commentaires
        item{
            // Ligne de séparation avant le titre
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
            Text(
                text = "Commentaires",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
        //  --------------  Liste des commentaires
        if(comments.isNotEmpty()){
            items(comments) { comment ->
                PostComment(
                    comment = comment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }else{
            //pas encore de comentaire
            item {
                Text(
                    text = "Aucun commentaire pour ce post.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 30.dp)
                )
            }
        }

        //  -------------- Section Ajouter un commentaire
        item {
            AddComment(
                onAddComment = onAddComment
            )
        }
    }
}

/**
 * Composable function to add a comment.
 * @param onAddComment Callback to handle adding a comment.
 */
@Composable
fun AddComment(
    onAddComment: (String) -> Unit, // Callback pour gérer l'ajout de commentaire
) {
    var commentText by remember { mutableStateOf("") } // Texte du commentaire

    Spacer(modifier = Modifier.height(36.dp))
    HorizontalDivider(
        modifier = Modifier.padding(bottom = 8.dp,start = 46.dp,end = 46.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Champ de saisie avec OutlinedTextField (multiligne)
        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text(text = "Ajouter un commentaire") },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = false,
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Bouton pour envoyer le commentaire
        Button(
            onClick = {
                if (commentText.isNotBlank()) {
                    onAddComment(commentText)
                    commentText = "" // Réinitialiser le champ après l'ajout
                }
            },
            enabled = commentText.isNotBlank(),
            modifier = Modifier
                .heightIn(60.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(top = 26.dp,bottom = 50.dp, start = 20.dp, end = 20.dp)
        ) {
            Text(text = "Envoyer")
        }
    }
}


/**
 * Composable function to display a single comment.
 * @param comment The comment to display.
 * @param modifier Modifier for styling and layout.
 */
@Composable
fun PostComment(comment: Comment, modifier: Modifier = Modifier) {
    // Convertir le timestamp en Date
    val date = Date(comment.timestamp)
    // Formater la date
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val formattedDate = remember { dateFormat.format(date) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Auteur du commentaire
            Text(
                text = comment.authorName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Date du commentaire
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Texte du commentaire
        Text(
            text = comment.content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PostDetailsScreenPreview() {
    val postId = "demo1"
    PostDetailsScreen(postId = postId, onBackClick = {}, userAuthId = null)

}