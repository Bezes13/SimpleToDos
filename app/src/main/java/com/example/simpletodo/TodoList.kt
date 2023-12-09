package com.example.simpletodo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max



@Composable
fun TodoList(
    todoList: List<String>,
    doneList: List<String>,
    updateList: (String, Boolean) -> Unit,
    markAsDone: (String) -> Unit
) {
    Column {
        todoList.forEach {
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp) // Increase the height for the text fields
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = (getFontSize(it.length)).sp,
                                    textDecoration = if (doneList.contains(it)) TextDecoration.LineThrough else TextDecoration.None,

                                    ),
                                maxLines = 1,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(10.dp)
                                    .widthIn(max = 285.dp), // Set a maximum width for the text
                            )

                            SmallFloatingActionButton(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterVertically),
                                onClick = { markAsDone(it) },
                                containerColor = Color.Gray
                            ) {
                                Icon(Icons.Filled.Check, contentDescription = "Mark as Done")
                            }
                        }
                    }

                    SmallFloatingActionButton(
                        onClick = { updateList(it, false) },
                        containerColor = Color.Red,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Filled.Clear, "Delete Todo")
                    }
                }
            }
        }
    }
}
@Composable
fun ScrollableTodoList(todoList: List<String>,
                       doneList: List<String>,
                       updateList: (String, Boolean) -> Unit,
                       markAsDone: (String) -> Unit){
    LazyColumn (Modifier.fillMaxHeight(0.8f)){
        items(1) {
            todoList.forEach {
                Divider()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp) // Increase the height for the text fields
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(horizontal = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxHeight(),
                            ) {
                                Text(
                                    text = it,
                                    style = TextStyle(
                                        fontSize = (getFontSize(it.length)).sp,
                                        textDecoration = if (doneList.contains(it)) TextDecoration.LineThrough else TextDecoration.None,

                                        ),
                                    maxLines = 1,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(10.dp)
                                        .widthIn(max = 285.dp), // Set a maximum width for the text
                                )

                                SmallFloatingActionButton(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterVertically),
                                    onClick = { markAsDone(it) },
                                    containerColor = Color.Gray
                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = "Mark as Done")
                                }
                            }
                        }

                        SmallFloatingActionButton(
                            onClick = { updateList(it, false) },
                            containerColor = Color.Red,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(Icons.Filled.Clear, "Delete Todo")
                        }
                    }
                }
            }
        }
    }
}

fun getFontSize(length: Int): Int {
    if (length > 15) {
        return max(15, 45 - length)
    }
    return 30
}

@Preview
@Composable
fun PreviewToDoList() {
    TodoList(
        todoList = listOf(
            "123456789012345",
            "12345678901234567",
            "1234567890123456789",
            "123456789012345678901",
            "very long data very long data very long data"
        ), doneList = listOf("more data"), updateList = { _, _ -> }, markAsDone = {})
}