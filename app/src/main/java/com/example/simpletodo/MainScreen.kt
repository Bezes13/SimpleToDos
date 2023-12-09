package com.example.simpletodo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpletodo.ui.theme.PurpleGrey80

@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {
    val retrievedList by mainViewModel.retrievedList.observeAsState(initial = emptyList())
    val doneList by mainViewModel.doneList.observeAsState(initial = emptyList())
    MainScreen(mainViewModel::updateList, retrievedList, doneList, mainViewModel::markAsDone)
}

@Composable
fun MainScreen(
    updateList: (String, Boolean) -> Unit,
    todoList: List<String>,
    doneList: List<String>,
    markAsDone: (String) -> Unit
) {
    var newTodoText by remember { mutableStateOf("") }
    Column {
        Text(
            text = stringResource(id = R.string.Header),
            fontSize = 50.sp,
            style = TextStyle(
                color = Color.Black, shadow = Shadow(
                    PurpleGrey80, Offset(2f, 2f)
                ),
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        todoList.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()){
                        Row (
                            Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxHeight()){
                            Text(
                                text = it,
                                style = TextStyle(
                                    textDecoration = if (doneList.contains(it)) TextDecoration.LineThrough else TextDecoration.None
                                ),
                                modifier = Modifier.align(Alignment.CenterVertically) // Takes up available space
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

        Row (
            Modifier
                .fillMaxWidth()
                .height(50.dp)){
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                label = { Text(stringResource(id = R.string.newTodo)) },
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            )
            SmallFloatingActionButton(
                onClick = { updateList(newTodoText, true) },
                containerColor = Color.Green,
                contentColor = Color.Black,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(Icons.Filled.Add, "Add new TODO")
            }
        }
    }
}

@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen({ _, _ -> },
        listOf("Koks essen", "Kokain Bär sehen", "Apps Coden"),
        listOf("Kokain Bär sehen"),
        { _ -> })
}
