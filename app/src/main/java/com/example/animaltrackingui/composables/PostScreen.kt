package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.db.PostStatusMap

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostScreen(
    username: String,
    timestamp: Long,
    animalType: String,
    postStatus: PostStatus,
    animalImage: String,
    latitude: Double,
    longitude: Double,
    location: List<String>,
    hostile: Boolean,
    animalName: String?,
    animalAge: Int?,
    description: String?,
    contactInfo: String?,
    comment: String?,
    reportCallback: (Int) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(15.dp)
        ) {
            PostInfo(
                username = username,
                timestamp = timestamp,
                reportCallback = {
                    reportCallback(1)
                })
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 5.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (animalName != null && animalAge != null) "$animalName, $animalType $animalAge" else animalType,
                    style = MaterialTheme.typography.headlineSmall
                )
                if(hostile){
                    AnimalHostileMark()
                }
            }
//            if (hostile) {
//                Text(
//                    text = "This Animal Has Been Marked As Hostile By The Poster",
//                    style = MaterialTheme.typography.labelMedium,
//                    color = Color.Red
//                )
//            }
            if (postStatus != PostStatus.NONE) {
                Text(
                    text = "Official Post Status: ${PostStatusMap.mapToString[postStatus]}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            if (description != null && contactInfo != null) {
                Text(text = description)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Contact Information",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(text = contactInfo)
            }
            if (comment != null) {
                Text(text = comment)
            }
            GlideImage(
                model = animalImage,
                contentDescription = "Post Animal Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Fit
            )
            PostLocation(
                latitude = latitude,
                longitude = longitude,
                location = location
            )
        }

    }
}