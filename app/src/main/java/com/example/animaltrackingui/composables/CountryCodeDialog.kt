package com.example.animaltrackingui.composables

import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.animaltrackingui.db.CountryCode
import com.example.animaltrackingui.db.getFlags
import com.example.animaltrackingui.db.getListOfCountries
import com.example.animaltrackingui.db.searchCountryList
import com.example.animaltrackingui.ui.theme.AppTheme

@Composable
fun CountryCodeDialog(
    modifier: Modifier = Modifier,
    isOnlyFlagShow: Boolean = false,
    defaultSelectedCountry: CountryCode = getListOfCountries().first(),
    pickedCountry: (CountryCode) -> Unit,
    dialogSearch: Boolean = true,
    dialogRounded: Int = 12
) {
    val countryList: List<CountryCode> = getListOfCountries()
    var isPickCountry by remember { mutableStateOf(defaultSelectedCountry) }
    var isOpenDialog by remember { mutableStateOf(false) }
    var searchValue by remember { mutableStateOf("") }
    LaunchedEffect(key1 = defaultSelectedCountry) {
        isPickCountry = defaultSelectedCountry
    }
    if (isOnlyFlagShow) {
        Row(modifier = Modifier.border(color = Color.White, width = 1.dp, shape = RoundedCornerShape(10.dp)).padding(8.dp).clickable { isOpenDialog = true }, verticalAlignment = Alignment.CenterVertically,) {
            Image(
                painter = painterResource(id = getFlags(isPickCountry.countryCode)),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")

        }
    } else {
        CountryTextField(
            fieldValue = isPickCountry.countryName,
            icon = isPickCountry.countryCode,
            onClick = {
                isOpenDialog = true
            })
    }
    if (isOpenDialog) {
        Dialog(
            onDismissRequest = { isOpenDialog = false },
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(dialogRounded.dp)
            ) {
                Column {
                    if (dialogSearch) {
                        searchValue = dialogSearchView()
                    }
                    LazyColumn {
                        items(
                            (if (searchValue.isEmpty()) {
                                countryList
                            } else {
                                countryList.searchCountryList(searchValue)
                            })
                        ) { countryItem ->
                            Row(
                                Modifier
                                    .padding(
                                        horizontal = 18.dp,
                                        vertical = 18.dp
                                    )
                                    .clickable {
                                        pickedCountry(countryItem)
                                        isPickCountry = countryItem
                                        isOpenDialog = false
                                    }) {
                                Image(
                                    painter = painterResource(
                                        id = getFlags(
                                            countryItem.countryCode
                                        )
                                    ), contentDescription = null
                                )
                                Text(
                                    countryItem.countryName,
                                    Modifier.padding(horizontal = 18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun dialogSearchView(): String {
    var searchValue by remember { mutableStateOf("") }
    Row {
        MyCustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            value = searchValue,
            onValueChange = {
                searchValue = it
            },
            fontSize = 14.sp,
            hint = "Search ...",
            textAlign = TextAlign.Start,
        )
    }
    return searchValue
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "",
    fontSize: TextUnit = 16.sp,
    textAlign: TextAlign = TextAlign.Start
) {
    Box(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                textAlign = textAlign,
                fontSize = fontSize
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Black.copy(0.2f)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        if (value.isEmpty()) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.then(
                    Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 52.dp)
                )
            )
        }
    }
}

@Composable
private fun CountryTextField(
    modifier: Modifier = Modifier,
    fieldValue: String,
    icon: String,
    onClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        label = {
            Text(text = "Country")
        },
        value = fieldValue,
        onValueChange = {},
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            //For Icons
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Image(painter = painterResource(id = getFlags(icon)), contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        enabled = false,
        trailingIcon = {
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
        }
    )
}

@Preview
@Composable
private fun preview() {
    AppTheme {
        Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CountryCodeDialog(
                pickedCountry = {},
                isOnlyFlagShow = true
            )
        }
    }
}
