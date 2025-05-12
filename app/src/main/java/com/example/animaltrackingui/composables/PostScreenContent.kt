package com.example.animaltrackingui.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.R
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.db.PostStatusMap
import com.example.animaltrackingui.utils.TextStyles
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PostLocation(latitude: Double, longitude: Double, location: List<String>) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.End, modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            onClick = {
                val geoUri =
                    "https://maps.google.com/maps?q=loc:${latitude},${longitude}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                context.startActivity(intent)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location_small),
                contentDescription = "Post Map Marker"
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = location.drop(1).joinToString(", "),
                style = TextStyles.InformationText
            )
        }
    }
}

@Composable
fun PostInfo(
    username: String,
    timestamp: Long,
    reportCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(id = R.drawable.account_circle_small),
                contentDescription = "User Profile Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = username,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            ReportButton(onClick = reportCallback)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.clock_small),
                contentDescription = "Clock Icon",
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                text = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(timestamp),
                    ZoneId.systemDefault()
                ).format(
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                ),
                style = TextStyles.InformationText,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun DeletePostDialog(dismissCallback: () -> Unit, confirmCallback: () -> Unit) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Example Icon"
            )
        },
        title = {
            Text(text = "Delete Post?")
        },
        text = {
            Text(text = "Are you sure you want to delete this post? This action cannot be undone.")
        },
        onDismissRequest = dismissCallback,
        confirmButton = {
            TextButton(
                onClick = confirmCallback
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissCallback
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun ReportPostDialog(
    dismissCallback: () -> Unit,
    reportPostCallback: () -> Unit,
    reasonSelectCallback: (String) -> Unit
) {
    var selectedReportReason by remember {
        mutableStateOf("")
    }
    val reportReasons = listOf("Inappropriate Language", "User Abusing System", "Inaccurate Post")
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.report),
                contentDescription = "Report Icon"
            )
        },
        title = {
            Text(text = "Report Post")
        },
        text = {
            Column {
                reportReasons.forEach { reason ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.dot),
                                contentDescription = "Selection Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = reason,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        RadioButton(
                            selected = reason == selectedReportReason,
                            onClick = {
                                selectedReportReason = reason
                                reasonSelectCallback(reason)
                            })
                    }
                }
            }
        },
        onDismissRequest = dismissCallback,
        confirmButton = {
            TextButton(
                onClick = reportPostCallback
            ) {
                Text("Report")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissCallback
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun UpdatePostStatusDialog(
    selected: PostStatus,
    dismissCallback: () -> Unit,
    updatePostStatusCallback: () -> Unit,
    statusSelectCallback: (String) -> Unit
) {
    var selectedStatus = PostStatusMap.mapToString[selected]
    val status by remember {
        mutableStateOf(
            listOf(
                PostStatusMap.mapToString[PostStatus.INVESTIGATING]!!,
                PostStatusMap.mapToString[PostStatus.ANIMAL_NOT_FOUND]!!,
                PostStatusMap.mapToString[PostStatus.ANIMAL_FOUND_BY_OFFICIALS]!!
            )
        )
    }
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.report),
                contentDescription = "Update Icon"
            )
        },
        title = {
            Text(text = "Update Post Status")
        },
        text = {
            Column {
                status.forEach { status ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.dot),
                                contentDescription = "Selection Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = status,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        RadioButton(
                            selected = status == selectedStatus,
                            onClick = {
                                selectedStatus = status
                                statusSelectCallback(status)
                            })
                    }
                }
            }
        },
        onDismissRequest = dismissCallback,
        confirmButton = {
            TextButton(
                onClick = updatePostStatusCallback
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissCallback
            ) {
                Text("Dismiss")
            }
        }
    )
}