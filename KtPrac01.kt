package com.example.project001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.project001.ui.theme.Project001Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Makes content drawn edge-to-edge
        setContent {
            Project001Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ToggleAndInput(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ToggleAndInput(modifier: Modifier = Modifier) {
    val isBlackBackground = remember { mutableStateOf(false) }
    val toggleButton = if (isBlackBackground.value) Icons.Filled.Brightness2 else Icons.Filled.Brightness5
    val inputText = remember { mutableStateOf("") }
    val entries = remember { mutableStateListOf<String>() }
    val checkedStates = remember { mutableStateListOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isBlackBackground.value) Color.Black else Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Button(onClick = { isBlackBackground.value = !isBlackBackground.value }) {
                Icon(toggleButton, contentDescription = "Toggle background")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputText.value,
                onValueChange = { newText -> inputText.value = newText },
                label = {
                    Text(
                        "Enter something...",
                        color = if (!isBlackBackground.value) Color.Black else MaterialTheme.colorScheme.onBackground
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (inputText.value.isNotBlank()) {
                            entries.add(inputText.value)
                            checkedStates.add(false)
                            inputText.value = ""

                            // Scroll to bottom when new item added
                            coroutineScope.launch {
                                listState.animateScrollToItem(entries.size)
                            }
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = if (!isBlackBackground.value) Color.Black else MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = if (!isBlackBackground.value) Color.Black else MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = if (!isBlackBackground.value) Color.Black else MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = if (!isBlackBackground.value) Color.Black else MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = if (!isBlackBackground.value) Color.Black.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(entries) { i, entry ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checkedStates[i],
                            onCheckedChange = { checked ->
                                checkedStates[i] = checked
                            }
                        )
                        Text(
                            text = entry,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (checkedStates[i])
                                    TextDecoration.LineThrough
                                else
                                    TextDecoration.None,
                                color = if (!isBlackBackground.value) Color.Black else MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleAndInputPreview() {
    Project001Theme {
        ToggleAndInput()
    }
}
