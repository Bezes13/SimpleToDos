package com.example.simpletodo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {
    val retrievedList by mainViewModel.retrievedList.observeAsState(initial = emptyList())
    val doneList by mainViewModel.doneList.observeAsState(initial = emptyList())
    MainScreen(mainViewModel::updateList, retrievedList, doneList, mainViewModel::markAsDone)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    updateList: (String, Boolean) -> Unit,
    todoList: List<String>,
    doneList: List<String>,
    markAsDone: (String) -> Unit
) {
    var newTodoText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column {
        Text(
            text = stringResource(id = R.string.Header),
            fontSize = 50.sp,
            style = TextStyle(
                color = Color.White, fontWeight = FontWeight.ExtraBold,

                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        ScrollableTodoList(
            updateList = updateList,
            doneList = doneList,
            todoList = todoList,
            markAsDone = markAsDone
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .align(Alignment.CenterVertically)
            )

            SmallFloatingActionButton(
                onClick = {
                    updateList(newTodoText, true)
                    newTodoText = ""
                    keyboardController?.hide()
                },
                containerColor = Color.Green,
                contentColor = Color.Black,
                modifier = Modifier
                    .size(50.dp) // Passen Sie die Größe nach Bedarf an
                    .align(Alignment.CenterVertically)
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
        listOf(
            "Koks essen",
            "Kokain Bär sehen",
            "sehr sehr sehr extrem langer text, der Probleme macht",
            "Apps Coden"
        ),
        listOf("Kokain Bär sehen"),
        { _ -> })
}
