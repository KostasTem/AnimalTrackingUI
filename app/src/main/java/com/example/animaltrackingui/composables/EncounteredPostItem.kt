package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.ui.theme.AppTheme

@Composable
fun EncounteredPostItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    post: EncounteredPost
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            .clickable(onClick = onClick)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                PostItemImage(image = post.animalImage)
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.width(160.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    PostItemInfo(postTitle = post.animalType, location = post.location[2], username = post.username, postStatus = post.postStatus, postAnimalHostile = post.hostile)
                }
            }
            Spacer(modifier = Modifier.width(30.dp))
            PostItemTimeAgoField(timestamp = post.timestamp!!)
        }
    }

}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        EncounteredPostItem(
            post = EncounteredPost(
                0,
                "Giorgos",
                "Dog",
                "https://media.newyorker.com/photos/62c4511e47222e61f46c2daa/4:3/w_2663,h_1997,c_limit/shouts-animals-watch-baby-hemingway.jpg",
                "Comment",
                34.2,
                27.1,
                listOf("Greece","Attica","Glufada"),
                "GR",
                System.currentTimeMillis()-1000,
                false,
                false,
                PostStatus.NONE
            )
        )
    }
}