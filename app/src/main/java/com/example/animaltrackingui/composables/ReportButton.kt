package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.R
import com.example.animaltrackingui.ui.theme.AppTheme

@Composable
fun ReportButton(onClick: () -> Unit, modifier:Modifier = Modifier){
    IconButton(onClick = onClick, modifier = modifier.clip(RoundedCornerShape(10.dp)).size(24.dp).background(color = MaterialTheme.colorScheme.errorContainer)) {
        Icon(painter = painterResource(id = R.drawable.report), contentDescription = "Report Icon", tint = MaterialTheme.colorScheme.error)
    }
}


@Composable
@Preview
private fun preview(){
    AppTheme {
        ReportButton(onClick = { /*TODO*/ })
    }
}