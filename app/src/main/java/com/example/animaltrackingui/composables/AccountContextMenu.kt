package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.animaltrackingui.R
import com.example.animaltrackingui.db.AppUser

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AccountContextMenu(navController: NavHostController, onLogoutClick: () -> Unit, user:AppUser, imagePath: String? = null, showUserPostsOnly: Boolean, onShowUserPostsOnlyChanged: (Boolean) -> Unit) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    IconButton(
        onClick = {
            isContextMenuVisible = true
        }
    ) {
        if (imagePath == null) {
            Icon(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "Account",
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(100.dp)
                    )
            ) {
                GlideImage(
                    model = imagePath,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .requiredWidth(24.dp)
                        .requiredHeight(24.dp)
                        .clip(RoundedCornerShape(90.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
    DropdownMenu(
        modifier = Modifier,
        expanded = isContextMenuVisible,
        onDismissRequest = {
            isContextMenuVisible = false
        }
    ) {
        DropdownMenuItem(text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Show My Posts Only", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.weight(1f))
                Checkbox(checked = showUserPostsOnly, onCheckedChange = onShowUserPostsOnlyChanged)
            }
        }, onClick = {})
        if(user.roles.contains("GOV")) {
            DropdownMenuItem(text = {
                Text(text = "GOV", color = MaterialTheme.colorScheme.onSurface)
            }, onClick = {
                isContextMenuVisible = false
                navController.navigate("GovUI")
            })
        }
        DropdownMenuItem(text = {
            Text(text = "Logout", color = MaterialTheme.colorScheme.onSurface)
        }, onClick = {
            isContextMenuVisible = false
            onLogoutClick()
        })
    }
}
