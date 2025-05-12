package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.animaltrackingui.R
import com.example.animaltrackingui.Utils
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.utils.TextStyles

@Composable
fun PostItemInfo(postTitle: String, location: String, username: String, postStatus: PostStatus, postAnimalHostile: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)){
        PostStatusField(postStatus = postStatus)
        if(postAnimalHostile){
            AnimalHostileMark()
        }
    }
    Text(
        text = postTitle,
        fontFamily = FontFamily.Default,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.location_small),
            contentDescription = "Post Map Marker",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = location,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.account_circle_small),
            contentDescription = "User Profile Icon",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = username,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostItemImage(image: String) {
    Box(
        modifier = Modifier.background(
            color = Color.Transparent,
            shape = RoundedCornerShape(40.dp)
        )
    ) {
        GlideImage(
            model = image,
            contentDescription = "Animal Image",
            modifier = Modifier
                .requiredWidth(75.dp)
                .requiredHeight(75.dp)
                .clip(RoundedCornerShape(90.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PostItemTimeAgoField(timestamp: Long) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.clock_small),
            contentDescription = "Clock Icon",
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text = "${Utils.calculateTimeAgo(timestamp)}",
            style = TextStyles.InformationText,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun PostStatusField(postStatus: PostStatus) {
    when (postStatus) {
        PostStatus.INVESTIGATING -> {
            PostInvestigated()
        }

        PostStatus.ANIMAL_NOT_FOUND -> {
            AnimalNotFound()
        }

        PostStatus.ANIMAL_FOUND_BY_OFFICIALS -> {
            AnimalFound()
        }

        else -> {

        }
    }
}

@Composable
fun AnimalHostileMark() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .wrapContentWidth()
            .background(color = Color.Red, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_priority_high_24),
            contentDescription = "Hostile Animal Icon",
            tint = Color.White,
            modifier = Modifier.width(12.dp).height(12.dp)
        )
        Text(text = "Hostile", color = Color.White, fontSize = 10.sp)
    }
}

@Composable
fun PostInvestigated() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .wrapContentWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Post Investigated Icon",
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.width(12.dp).height(12.dp)
        )
        Text(text = "Investigating", color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 10.sp)
    }
}

@Composable
fun AnimalFound() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .wrapContentWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.found),
            contentDescription = "Post Investigated Icon",
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(text = "Found", color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 10.sp)
    }
}

@Composable
fun AnimalNotFound() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .wrapContentWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.not_found),
            contentDescription = "Post Investigated Icon",
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(text = "Not Found", color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 10.sp)
    }
}