package com.example.animaltrackingui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.animaltrackingui.R
import com.example.animaltrackingui.db.AppUser

@Composable
fun PostScreenBottomBar(postUsername: String, user: AppUser?, buttonClickCallback: (Int) -> Unit) {
    if (postUsername == user?.username || (user != null && user.roles.contains("GOV"))) {
        NavigationBar {
            if (user != null && user.roles.contains("GOV")) {
                NavigationBarItem(
                    selected = false,
                    onClick = { buttonClickCallback(2) },
                    icon = { Icon(painter = painterResource(id = R.drawable.gov_24), contentDescription = "GovIcon") },
                    label = {
                        Text(
                            text = "Update Status"
                        )
                    })
            }
            if (postUsername == user?.username){
                NavigationBarItem(
                    selected = false,
                    onClick = { buttonClickCallback(0) },
                    icon = { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Post Button") },
                    label = {
                        Text(
                            text = "Delete Post"
                        )
                    })
            }
        }
    }
}