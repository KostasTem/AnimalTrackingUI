package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.ui.theme.applyTonalElevation

@Composable
fun TopTabRow(
    modifier: Modifier = Modifier,
    periodIndex: Int,
    onPeriodIndexChanged: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = periodIndex,
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        containerColor = MaterialTheme.colorScheme.applyTonalElevation(
            backgroundColor = MaterialTheme.colorScheme.surface,
            elevation = 3.dp
        ),
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[periodIndex])
                    .height(4.dp)
                    .padding(horizontal = 80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        },
        divider = {}) {
        Tab(
            selected = periodIndex == 0,
            onClick = {
                onPeriodIndexChanged(0)
            },
            text = { Text(text = "Missing") },
            selectedContentColor = MaterialTheme.colorScheme.onBackground,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Tab(
            selected = periodIndex == 1,
            onClick = {
                onPeriodIndexChanged(1)
            },
            text = { Text(text = "Encountered") },
            selectedContentColor = MaterialTheme.colorScheme.onBackground,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}