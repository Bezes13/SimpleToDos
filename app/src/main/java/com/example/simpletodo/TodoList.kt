package com.example.simpletodo

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.max

@Composable
fun ScrollableTodoList(
    todoList: List<String>,
    doneList: List<String>,
    updateList: (String, Boolean) -> Unit,
    markAsDone: (String) -> Unit
) {
    LazyColumn(Modifier.fillMaxHeight(0.85f)) {
        item {
            todoList.forEach {
                TodoListPosition(it, doneList, updateList, markAsDone)
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
    ScrollableTodoList(
        todoList = listOf(
            "123456789012345",
            "12345678901234567",
            "1234567890123456789",
            "123456789012345678901",
            "very long data very long data very long data",
            "very long data very long data very long data mehr mehr merh askdjaosdmasdmlmads"
        ), doneList = listOf("more data"), updateList = { _, _ -> }, markAsDone = {})
}