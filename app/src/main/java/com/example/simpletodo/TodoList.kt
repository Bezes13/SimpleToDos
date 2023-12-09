package com.example.simpletodo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodoList(todoList: List<String>,doneList: List<String>, updateList: (String, Boolean)-> Unit, markAsDone: (String) -> Unit){
    todoList.forEach {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Increase the height for the text fields
            ) {
                Box(modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()) {
                    Row (
                        Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxHeight()) {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 30.sp, // Increase the font size
                                textDecoration = if (doneList.contains(it)) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        SmallFloatingActionButton(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
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