package com.example.testingproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testingproject.ui.theme.TestingProjectTheme
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions

data class TodoItem(val text: String, val checked: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TodoListScreen(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var todoItems by remember { mutableStateOf(listOf<TodoItem>()) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter a to-do item") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val trimmed = text.trim()
                    if (trimmed.isNotEmpty()) {
                        todoItems = todoItems + TodoItem(trimmed)
                        text = ""
                    }
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            itemsIndexed(todoItems) { index, item ->
                TodoRow(
                    item = item,
                    onCheckedChange = {
                        // Remove the item when checked
                        todoItems = todoItems.toMutableList().also { it.removeAt(index) }
                    }
                )
            }
        }
    }
}

@Composable
fun TodoRow(item: TodoItem, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically // Center vertically!
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = item.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    TestingProjectTheme {
        TodoListScreen()
    }
}
